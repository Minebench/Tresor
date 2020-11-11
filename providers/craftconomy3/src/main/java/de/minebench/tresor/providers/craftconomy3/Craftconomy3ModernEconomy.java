package de.minebench.tresor.providers.craftconomy3;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2020 Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.groups.WorldGroupsManager;
import com.greatmancode.craftconomy3.tools.interfaces.BukkitLoader;
import de.minebench.tresor.Provider;
import de.minebench.tresor.economy.EconomyResponse;
import de.minebench.tresor.economy.ModernEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static de.minebench.tresor.TresorUtils.asyncFuture;
import static de.minebench.tresor.TresorUtils.future;

public class Craftconomy3ModernEconomy extends Provider<ModernEconomy, BukkitLoader> implements ModernEconomy {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(128);
    private BukkitLoader hooked;

    public Craftconomy3ModernEconomy() {
        super(ModernEconomy.class);
    }

    private Account getAccount(String playerName) {
        return Common.getInstance().getAccountManager().getAccount(playerName, false);
    }

    private Account getAccount(UUID playerId) {
        return Common.getInstance().getStorageHandler().getStorageEngine().getAccount(playerId);
    }

    private BigDecimal getBalanceInternal(String playerName, String worldName) {
        Account account = getAccount(playerName);
        if (account == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(account.getBalance(worldName, getCurrency(worldName).getName()));
    }

    private BigDecimal getBalanceInternal(UUID playerId, String worldName) {
        Account account = getAccount(playerId);
        if (account == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(account.getBalance(worldName, getCurrency(worldName).getName()));
    }

    private String getName(OfflinePlayer offlinePlayer) {
        String name = offlinePlayer.getName();
        if (name == null) {
            Account account = getAccount(offlinePlayer.getUniqueId());
            if (account != null) {
                name = account.getAccountName();
            }
        }
        return name;
    }

    private String getName(UUID playerId) {
        Player player = Bukkit.getServer().getPlayer(playerId);
        if (player != null) {
            return player.getName();
        }
        Account account = getAccount(playerId);
        if (account != null) {
            return account.getAccountName();
        }
        return null;
    }

    private Currency getCurrency(String worldName) {
        String worldGroupName = Common.getInstance().getWorldGroupManager().getWorldGroupName(worldName);
        Currency currency = null;
        if (!worldGroupName.equals("default")) {
            currency = Common.getInstance().getCurrencyManager().getCurrency(worldGroupName);
        }
        if (currency == null) {
            currency = Common.getInstance().getCurrencyManager().getDefaultCurrency();
        }
        return currency;
    }

    @Override
    public boolean isEnabled() {
        return hooked != null ? hooked.isEnabled() : Bukkit.getServer().getPluginManager().isPluginEnabled("CraftConomy");
    }

    @Override
    public BukkitLoader getHooked() {
        if (hooked == null) {
            hooked = (BukkitLoader) Bukkit.getServer().getPluginManager().getPlugin(getName());
        }
        return hooked;
    }

    @Override
    public String getName() {
        return "Craftconomy3";
    }

    @Override
    public boolean supports(Feature feature) {
        switch (feature) {
            case WORLD:
            case LOG:
            case UUID:
            case OFFLINE:
            case BANK:
                return true;
        }
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(BigDecimal amount) {
        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        Currency defaultCurrency = Common.getInstance().getCurrencyManager().getDefaultCurrency();

        return Common.getInstance().format(worldName, defaultCurrency, amount.doubleValue());
    }

    @Override
    public String currencyNamePlural() {
        return Common.getInstance().getCurrencyManager().getDefaultCurrency().getPlural();
    }

    @Override
    public String currencyNameSingular() {
        return Common.getInstance().getCurrencyManager().getDefaultCurrency().getName();
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(String playerName) {
        return asyncFuture(getHooked(), () -> playerName != null && Common.getInstance().getAccountManager().exist(playerName, false));
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getUniqueId());
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(UUID playerId) {
        return asyncFuture(getHooked(), () -> getAccount(playerId) != null);
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(String playerName, String world) {
        return hasAccount(playerName);
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(UUID playerId, String worldName) {
        return hasAccount(playerId);
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(OfflinePlayer offlinePlayer, String world) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(String playerName) {
        return getBalance(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME);
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getUniqueId(), WorldGroupsManager.DEFAULT_GROUP_NAME);
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(UUID playerId) {
        return getBalance(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME);
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(String playerName, String worldName) {
        return asyncFuture(getHooked(), () -> getBalanceInternal(playerName, worldName));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer.getUniqueId(), worldName);
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(UUID playerId, String worldName) {
        return asyncFuture(getHooked(), () -> {
            Account account = getAccount(playerId);
            if (account == null) {
                return ZERO;
            }
            return BigDecimal.valueOf(account.getBalance(worldName, getCurrency(worldName).getName()));
        });
    }

    @Override
    public CompletableFuture<Boolean> has(String playerName, BigDecimal amount) {
        return has(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public CompletableFuture<Boolean> has(OfflinePlayer offlinePlayer, BigDecimal amount) {
        return has(offlinePlayer, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public CompletableFuture<Boolean> has(UUID playerId, BigDecimal amount) {
        return has(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public CompletableFuture<Boolean> has(String playerName, String worldName, BigDecimal amount) {
        return asyncFuture(getHooked(), () -> {
            Account account = getAccount(playerName);
            return account != null && account.hasEnough(amount.doubleValue(), worldName, getCurrency(worldName).getName());
        });
    }

    @Override
    public CompletableFuture<Boolean> has(OfflinePlayer offlinePlayer, String worldName, BigDecimal amount) {
        return has(offlinePlayer.getUniqueId(), worldName, amount);
    }

    @Override
    public CompletableFuture<Boolean> has(UUID playerId, String worldName, BigDecimal amount) {
        return asyncFuture(getHooked(), () -> {
            Account account = getAccount(playerId);
            return account != null && account.hasEnough(amount.doubleValue(), worldName, getCurrency(worldName).getName());
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(String playerName, BigDecimal amount, String reason) {
        return withdrawPlayer(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(OfflinePlayer offlinePlayer, BigDecimal amount, String reason) {
        return withdrawPlayer(offlinePlayer.getUniqueId(), WorldGroupsManager.DEFAULT_GROUP_NAME, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(UUID playerId, BigDecimal amount, String reason) {
        return withdrawPlayer(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(String playerName, String worldName, BigDecimal amount, String reason) {
        return future(() -> {
            if (amount.compareTo(ZERO) < 0) {
                return new EconomyResponse(ZERO, getBalanceInternal(playerName, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
            }

            double balance;
            Account account = getAccount(playerName);
            Currency currency = getCurrency(worldName);
            if (account.hasEnough(amount.doubleValue(), worldName, currency.getName())) {
                balance = account.withdraw(amount.doubleValue(), worldName, currency.getName(), Cause.VAULT, null);
                return new EconomyResponse(amount, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, "");
            } else {
                return new EconomyResponse(ZERO, getBalanceInternal(playerName, worldName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
            }
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, BigDecimal amount, String reason) {
        return withdrawPlayer(offlinePlayer.getUniqueId(), worldName, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(UUID playerId, String worldName, BigDecimal amount, String reason) {
        return asyncFuture(getHooked(), () -> {
            if (amount.compareTo(ZERO) < 0) {
                return new EconomyResponse(ZERO, getBalanceInternal(playerId, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
            }

            double balance;
            Account account = getAccount(playerId);
            Currency currency = getCurrency(worldName);
            if (account.hasEnough(amount.doubleValue(), worldName, currency.getName())) {
                balance = account.withdraw(amount.doubleValue(), worldName, currency.getName(), Cause.VAULT, null);
                return new EconomyResponse(amount, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, "");
            } else {
                return new EconomyResponse(ZERO, getBalanceInternal(playerId, worldName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
            }
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(String playerName, BigDecimal amount, String reason) {
        return depositPlayer(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(OfflinePlayer offlinePlayer, BigDecimal amount, String reason) {
        return depositPlayer(offlinePlayer, WorldGroupsManager.DEFAULT_GROUP_NAME, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(UUID playerId, BigDecimal amount, String reason) {
        return depositPlayer(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(String playerName, String worldName, BigDecimal amount, String reason) {
        return future(() -> {
            if (amount.compareTo(ZERO) < 0) {
                return new EconomyResponse(BigDecimal.ZERO, getBalanceInternal(playerName, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
            }

            Account account = getAccount(playerName);

            double balance = account.deposit(amount.doubleValue(), worldName, getCurrency(worldName).getName(), Cause.VAULT, reason);
            return new EconomyResponse(amount, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, null);
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(OfflinePlayer offlinePlayer, String worldName, BigDecimal amount, String reason) {
        return depositPlayer(offlinePlayer.getUniqueId(), worldName, amount, reason);
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(UUID playerId, String worldName, BigDecimal amount, String reason) {
        return asyncFuture(getHooked(), () -> {
            if (amount.compareTo(ZERO) < 0) {
                return new EconomyResponse(ZERO, getBalanceInternal(playerId, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
            }

            Account account = getAccount(playerId);

            double balance = account.deposit(amount.doubleValue(), worldName, getCurrency(worldName).getName(), Cause.VAULT, reason);
            return new EconomyResponse(amount, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, null);
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> createBank(String bankName, String playerName) {
        return asyncFuture(getHooked(), () -> {
            boolean success = false;
            if (!Common.getInstance().getAccountManager().exist(bankName, true)) {
                Common.getInstance().getAccountManager().getAccount(bankName, true).getAccountACL().set(playerName, true, true, true, true, true);
                success = true;
            }
            if (success) {
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.SUCCESS, "");
            }

            return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "Unable to create that bank account. It already exists!");
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> createBank(String name, UUID playerId) {
        return null;
    }

    @Override
    public CompletableFuture<EconomyResponse> createBank(String bankName, OfflinePlayer offlinePlayer) {
        return createBank(bankName, getName(offlinePlayer));
    }

    @Override
    public CompletableFuture<EconomyResponse> deleteBank(String bankName) {
        return asyncFuture(getHooked(), () -> {
            boolean success = Common.getInstance().getAccountManager().delete(bankName, true);
            if (success) {
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.SUCCESS, "");
            }

            return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "Unable to delete that bank account.");
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> bankBalance(String bankName) {
        return asyncFuture(getHooked(), () -> {
            if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                double balance = Common.getInstance().getAccountManager().getAccount(bankName, true)
                        .getBalance(WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName());
                return new EconomyResponse(ZERO, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, "");
            }
            return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> bankHas(String bankName, BigDecimal amount) {
        return asyncFuture(getHooked(), () -> {
            if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                Account account = Common.getInstance().getAccountManager().getAccount(bankName, true);
                if (account.hasEnough(amount.doubleValue(), Common.getInstance().getServerCaller().getDefaultWorld(), Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName())) {
                    return new EconomyResponse(ZERO, bankBalance(bankName).get().getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
                } else {
                    return new EconomyResponse(ZERO, bankBalance(bankName).get().getBalance(), EconomyResponse.ResponseType.FAILURE, "The bank does not have enough money!");
                }
            }
            return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> bankWithdraw(String bankName, BigDecimal amount, String reason) {
        return future(() -> {
            if (amount.compareTo(ZERO) < 0) {
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
            }

            EconomyResponse er = bankHas(bankName, amount).get();
            if (!er.transactionSuccess()) {
                return er;
            } else {
                if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                    double balance = Common.getInstance().getAccountManager().getAccount(bankName, true)
                            .withdraw(amount.doubleValue(), WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName(), Cause.VAULT, reason);
                    return new EconomyResponse(amount, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, "");
                }
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
            }
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> bankDeposit(String bankName, BigDecimal amount, String reason) {
        return future(() -> {
            if (amount.compareTo(ZERO) < 0) {
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
            }

            if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                double balance = Common.getInstance().getAccountManager().getAccount(bankName, true)
                        .deposit(amount.doubleValue(),WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName(), Cause.VAULT, reason);
                return new EconomyResponse(amount, BigDecimal.valueOf(balance), EconomyResponse.ResponseType.SUCCESS, "");
            }
            return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankOwner(String bankName, String playerName) {
        return future(() -> {
            if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                if (Common.getInstance().getAccountManager().getAccount(bankName, true).getAccountACL().isOwner(playerName)) {
                    return new EconomyResponse(ZERO, bankBalance(bankName).get().getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
                }
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "This player is not the owner of the bank!");
            }
            return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankOwner(String bankName, UUID playerId) {
        return isBankOwner(bankName, getName(playerId));
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankOwner(String bankName, OfflinePlayer offlinePlayer) {
        return isBankOwner(bankName, getName(offlinePlayer));
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankMember(String bankName, String playerName) {
        return future(() -> {
            EconomyResponse er = isBankOwner(bankName, playerName).get();
            if (er.transactionSuccess()) {
                return er;
            } else {
                if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                    Account account = Common.getInstance().getAccountManager().getAccount(bankName, true);
                    if (account.getAccountACL().canDeposit(playerName) && account.getAccountACL().canWithdraw(playerName)) {
                        return new EconomyResponse(ZERO, bankBalance(bankName).get().getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
                    }
                }
                return new EconomyResponse(ZERO, ZERO, EconomyResponse.ResponseType.FAILURE, "This player is not a member of the bank!");
            }
        });
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankMember(String bankName, UUID playerId) {
        return isBankMember(bankName, getName(playerId));
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankMember(String bankName, OfflinePlayer offlinePlayer) {
        return isBankMember(bankName, getName(offlinePlayer));
    }

    @Override
    public CompletableFuture<List<String>> getBanks() {
        return asyncFuture(getHooked(), () -> Common.getInstance().getAccountManager().getAllAccounts(true));
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(String playerName) {
        return asyncFuture(getHooked(), () -> {
            if (hasAccount(playerName).get()) {
                return false;
            }
            Common.getInstance().getAccountManager().getAccount(playerName, false);
            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(UUID playerId) {
        return asyncFuture(getHooked(), () -> {
            if (hasAccount(playerId).get()) {
                return false;
            }
            Account account = Common.getInstance().getAccountManager().getAccount(getName(playerId), false);
            Common.getInstance().getStorageHandler().getStorageEngine().updateUsername(account.getAccountName().toLowerCase(), playerId);
            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(OfflinePlayer offlinePlayer) {
        return asyncFuture(getHooked(), () -> {
            if (hasAccount(offlinePlayer).get()) {
                return false;
            }
            Account account = Common.getInstance().getAccountManager().getAccount(getName(offlinePlayer), false);
            Common.getInstance().getStorageHandler().getStorageEngine().updateUsername(account.getAccountName().toLowerCase(), offlinePlayer.getUniqueId());
            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(UUID playerId, String worldName) {
        return createPlayerAccount(playerId);
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return createPlayerAccount(offlinePlayer);
    }
}

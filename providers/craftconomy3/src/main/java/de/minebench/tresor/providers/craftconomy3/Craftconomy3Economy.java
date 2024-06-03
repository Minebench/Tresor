package de.minebench.tresor.providers.craftconomy3;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2024 Max Lee aka Phoenix616 (max@themoep.de)
 * Copyright (C) 2024 Tresor Contributors (https://github.com/Minebench/Tresor/graphs/contributors)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.groups.WorldGroupsManager;
import com.greatmancode.craftconomy3.tools.interfaces.BukkitLoader;
import de.minebench.tresor.Provider;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class Craftconomy3Economy extends Provider<Economy, BukkitLoader> implements Economy {

    public Craftconomy3Economy() {
        super(Economy.class);
    }

    private Account getAccount(String playerName) {
        return Common.getInstance().getAccountManager().getAccount(playerName, false);
    }

    private Account getAccount(UUID playerId) {
        return Common.getInstance().getStorageHandler().getStorageEngine().getAccount(playerId);
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
        return Bukkit.getServer().getPluginManager().isPluginEnabled("CraftConomy");
    }

    @Override
    public BukkitLoader getHooked() {
        return (BukkitLoader) Bukkit.getServer().getPluginManager().getPlugin(getName());
    }

    @Override
    public String getName() {
        return "Craftconomy3";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        String worldName = Common.getInstance().getServerCaller().getDefaultWorld();
        Currency defaultCurrency = Common.getInstance().getCurrencyManager().getDefaultCurrency();

        return Common.getInstance().format(worldName, defaultCurrency, amount);
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
    public boolean hasAccount(String playerName) {
        return playerName != null && Common.getInstance().getAccountManager().exist(playerName, false);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean hasAccount(UUID playerId) {
        return getAccount(playerId) != null;
    }

    @Override
    public boolean hasAccount(String playerName, String world) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getUniqueId(), WorldGroupsManager.DEFAULT_GROUP_NAME);
    }

    @Override
    public double getBalance(UUID playerId) {
        return getBalance(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME);
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        if (!hasAccount(playerName)) {
            return 0;
        }
        return Common.getInstance().getAccountManager().getAccount(playerName, false).getBalance(worldName, getCurrency(worldName).getName());
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer.getUniqueId(), worldName);
    }

    @Override
    public double getBalance(UUID playerId, String worldName) {
        Account account = getAccount(playerId);
        if (account == null) {
            return 0;
        }
        return account.getBalance(worldName, getCurrency(worldName).getName());
    }

    @Override
    public boolean has(String playerName, double amount) {
        return has(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return has(offlinePlayer, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public boolean has(UUID playerId, double amount) {
        return has(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        Account account = getAccount(playerName);
        return account != null && account.hasEnough(amount, worldName, getCurrency(worldName).getName());
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return has(offlinePlayer.getUniqueId(), worldName, amount);
    }

    @Override
    public boolean has(UUID playerId, String worldName, double amount) {
        Account account = getAccount(playerId);
        return account != null && account.hasEnough(amount, worldName, getCurrency(worldName).getName());
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return withdrawPlayer(offlinePlayer.getUniqueId(), WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(UUID playerId, double amount) {
        return withdrawPlayer(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(playerName, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        }

        double balance;
        Account account = getAccount(playerName);
        Currency currency = getCurrency(worldName);
        if (account.hasEnough(amount, worldName, currency.getName())) {
            balance = account.withdraw(amount, worldName, currency.getName(), Cause.VAULT, null);
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
        } else {
            return new EconomyResponse(0, getBalance(playerName, worldName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(offlinePlayer.getUniqueId(), worldName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(UUID playerId, String worldName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(playerId, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        }

        double balance;
        Account account = getAccount(playerId);
        Currency currency = getCurrency(worldName);
        if (account.hasEnough(amount, worldName, currency.getName())) {
            balance = account.withdraw(amount, worldName, currency.getName(), Cause.VAULT, null);
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
        } else {
            return new EconomyResponse(0, getBalance(playerId, worldName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(playerName, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return depositPlayer(offlinePlayer, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public EconomyResponse depositPlayer(UUID playerId, double amount) {
        return depositPlayer(playerId, WorldGroupsManager.DEFAULT_GROUP_NAME, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(playerName, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        }

        Account account = getAccount(playerName);

        double balance = account.deposit(amount, worldName, getCurrency(worldName).getName(), Cause.VAULT, null);
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer.getUniqueId(), worldName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(UUID playerId, String worldName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, getBalance(playerId, worldName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        }

        Account account = getAccount(playerId);

        double balance = account.deposit(amount, worldName, getCurrency(worldName).getName(), Cause.VAULT, null);
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse createBank(String bankName, String playerName) {
        boolean success = false;
        if (!Common.getInstance().getAccountManager().exist(bankName, true)) {
            Common.getInstance().getAccountManager().getAccount(bankName,true).getAccountACL().set(playerName, true, true, true, true, true);
            success = true;
        }
        if (success) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "");
        }

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Unable to create that bank account. It already exists!");
    }

    @Override
    public EconomyResponse createBank(String bankName, OfflinePlayer offlinePlayer) {
        return createBank(bankName, getName(offlinePlayer));
    }

    @Override
    public EconomyResponse deleteBank(String bankName) {
        boolean success = Common.getInstance().getAccountManager().delete(bankName, true);
        if (success) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "");
        }

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Unable to delete that bank account.");
    }

    @Override
    public EconomyResponse bankBalance(String bankName) {
        if (Common.getInstance().getAccountManager().exist(bankName, true)) {
            return new EconomyResponse(0, Common.getInstance().getAccountManager().getAccount(bankName, true).getBalance(WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName()), EconomyResponse.ResponseType.SUCCESS, "");
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
    }

    @Override
    public EconomyResponse bankHas(String bankName, double amount) {
        if (Common.getInstance().getAccountManager().exist(bankName, true)) {
            Account account = Common.getInstance().getAccountManager().getAccount(bankName, true);
            if (account.hasEnough(amount, Common.getInstance().getServerCaller().getDefaultWorld(), Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName())) {
                return new EconomyResponse(0, bankBalance(bankName).balance, EconomyResponse.ResponseType.SUCCESS, "");
            } else {
                return new EconomyResponse(0, bankBalance(bankName).balance, EconomyResponse.ResponseType.FAILURE, "The bank does not have enough money!");
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
    }

    @Override
    public EconomyResponse bankWithdraw(String bankName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        }

        EconomyResponse er = bankHas(bankName, amount);
        if (!er.transactionSuccess()) {
            return er;
        } else {
            if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                return new EconomyResponse(0, Common.getInstance().getAccountManager().getAccount(bankName, true).withdraw(amount,WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName(), Cause.VAULT, null), EconomyResponse.ResponseType.SUCCESS, "");
            }
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
        }
    }

    @Override
    public EconomyResponse bankDeposit(String bankName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        }

        if (Common.getInstance().getAccountManager().exist(bankName, true)) {
            return new EconomyResponse(0, Common.getInstance().getAccountManager().getAccount(bankName, true).deposit(amount,WorldGroupsManager.DEFAULT_GROUP_NAME, Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName(), Cause.VAULT, null), EconomyResponse.ResponseType.SUCCESS, "");
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, String playerName) {
        if (Common.getInstance().getAccountManager().exist(bankName, true)) {
            if (Common.getInstance().getAccountManager().getAccount(bankName, true).getAccountACL().isOwner(playerName)) {
                return new EconomyResponse(0, bankBalance(bankName).balance, EconomyResponse.ResponseType.SUCCESS, "");
            }
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "This player is not the owner of the bank!");
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "That bank does not exist!");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, OfflinePlayer offlinePlayer) {
        return isBankOwner(bankName, getName(offlinePlayer));
    }

    @Override
    public EconomyResponse isBankMember(String bankName, String playerName) {
        EconomyResponse er = isBankOwner(bankName, playerName);
        if (er.transactionSuccess()) {
            return er;
        } else {
            if (Common.getInstance().getAccountManager().exist(bankName, true)) {
                Account account = Common.getInstance().getAccountManager().getAccount(bankName, true);
                if (account.getAccountACL().canDeposit(playerName) && account.getAccountACL().canWithdraw(playerName)) {
                    return new EconomyResponse(0, bankBalance(bankName).balance, EconomyResponse.ResponseType.SUCCESS, "");
                }
            }
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "This player is not a member of the bank!");
        }
    }

    @Override
    public EconomyResponse isBankMember(String bankName, OfflinePlayer offlinePlayer) {
        return isBankMember(bankName, getName(offlinePlayer));
    }

    @Override
    public List<String> getBanks() {
        return Common.getInstance().getAccountManager().getAllAccounts(true);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        if (hasAccount(playerName)) {
            return false;
        }
        Common.getInstance().getAccountManager().getAccount(playerName, false);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if (hasAccount(offlinePlayer)) {
            return false;
        }
        Account account = Common.getInstance().getAccountManager().getAccount(getName(offlinePlayer), false);
        Common.getInstance().getStorageHandler().getStorageEngine().updateUsername(account.getAccountName().toLowerCase(), offlinePlayer.getUniqueId());
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return createPlayerAccount(offlinePlayer);
    }
}

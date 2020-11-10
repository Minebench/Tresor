package de.minebench.tresor.providers.tresor;

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

import de.minebench.tresor.Provider;
import de.minebench.tresor.Tresor;
import de.minebench.tresor.economy.EconomyResponse;
import de.minebench.tresor.economy.ModernEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TresorModernEconomy extends Provider<ModernEconomy, Tresor> implements ModernEconomy, Listener {

    private Economy economy = null;

    public TresorModernEconomy() {
        super(ModernEconomy.class, ServicePriority.Lowest);
        updateProvider();
    }

    private void updateProvider() {
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (provider != null && provider.getPlugin() != getHooked() ) { // makes sure to not hook into our own wrapper economy to avoid loops
            economy = provider.getProvider();
        } else {
            economy = null;
        }
    }

    @Override
    public void register() {
        super.register();
        Bukkit.getServer().getPluginManager().registerEvents(this, getHooked());
    }

    @Override
    public void unregister() {
        super.unregister();
        ServiceRegisterEvent.getHandlerList().unregister(this);
        ServiceUnregisterEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        updateProvider();
    }

    @EventHandler
    public void onServiceUnregister(ServiceUnregisterEvent event) {
        updateProvider();
    }

    private static <T> CompletableFuture<T> future(Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            future.complete(callable.call());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public boolean isEnabled() {
        return economy != null && economy.isEnabled();
    }

    @Override
    public Tresor getHooked() {
        return (Tresor) Bukkit.getPluginManager().getPlugin("Tresor");
    }

    @Override
    public String getName() {
        return "Tresor (" + (economy != null ? economy.getName() : "none") + ")";
    }

    @Override
    public boolean supports(Feature feature) {
        return feature == Feature.BANK && economy.hasBankSupport();
    }

    @Override
    public int fractionalDigits() {
        return economy.fractionalDigits();
    }

    @Override
    public String format(BigDecimal amount) {
        return economy.format(amount.doubleValue());
    }

    @Override
    public String currencyNamePlural() {
        return economy.currencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return economy.currencyNameSingular();
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(String playerName) {
        return future(() -> economy.hasAccount(playerName));
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(UUID playerId) {
        return future(() -> economy.hasAccount(playerId));
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(OfflinePlayer player) {
        return future(() -> economy.hasAccount(player));
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(String playerName, String worldName) {
        return future(() -> economy.hasAccount(playerName, worldName));
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(UUID playerId, String worldName) {
        return future(() -> economy.hasAccount(playerId, worldName));
    }

    @Override
    public CompletableFuture<Boolean> hasAccount(OfflinePlayer player, String worldName) {
        return future(() -> economy.hasAccount(player, worldName));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(UUID playerId) {
        return future(() -> BigDecimal.valueOf(economy.getBalance(playerId)));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(String playerName) {
        return future(() -> BigDecimal.valueOf(economy.getBalance(playerName)));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(OfflinePlayer player) {
        return future(() -> BigDecimal.valueOf(economy.getBalance(player)));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(String playerName, String world) {
        return future(() -> BigDecimal.valueOf(economy.getBalance(playerName, world)));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(UUID playerId, String world) {
        return future(() -> BigDecimal.valueOf(economy.getBalance(playerId, world)));
    }

    @Override
    public CompletableFuture<BigDecimal> getBalance(OfflinePlayer player, String world) {
        return future(() -> BigDecimal.valueOf(economy.getBalance(player, world)));
    }

    @Override
    public CompletableFuture<Boolean> has(String playerName, BigDecimal amount) {
        return future(() -> economy.has(playerName, amount.doubleValue()));
    }

    @Override
    public CompletableFuture<Boolean> has(UUID playerId, BigDecimal amount) {
        return future(() -> economy.has(playerId, amount.doubleValue()));
    }

    @Override
    public CompletableFuture<Boolean> has(OfflinePlayer player, BigDecimal amount) {
        return future(() -> economy.has(player, amount.doubleValue()));
    }

    @Override
    public CompletableFuture<Boolean> has(String playerName, String worldName, BigDecimal amount) {
        return future(() -> economy.has(playerName, worldName, amount.doubleValue()));
    }

    @Override
    public CompletableFuture<Boolean> has(UUID playerId, String worldName, BigDecimal amount) {
        return future(() -> economy.has(playerId, worldName, amount.doubleValue()));
    }

    @Override
    public CompletableFuture<Boolean> has(OfflinePlayer player, String worldName, BigDecimal amount) {
        return future(() -> economy.has(player, worldName, amount.doubleValue()));
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(String playerName, BigDecimal amount, String reason) {
        return future(() -> economy.withdrawPlayer(playerName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(UUID playerId, BigDecimal amount, String reason) {
        return future(() -> economy.withdrawPlayer(playerId, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(OfflinePlayer player, BigDecimal amount, String reason) {
        return future(() -> economy.withdrawPlayer(player, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(String playerName, String worldName, BigDecimal amount, String reason) {
        return future(() -> economy.withdrawPlayer(playerName, worldName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(UUID playerId, String worldName, BigDecimal amount, String reason) {
        return future(() -> economy.withdrawPlayer(playerId, worldName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> withdrawPlayer(OfflinePlayer player, String worldName, BigDecimal amount, String reason) {
        return future(() -> economy.withdrawPlayer(player, worldName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(String playerName, BigDecimal amount, String reason) {
        return future(() -> economy.depositPlayer(playerName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(UUID playerId, BigDecimal amount, String reason) {
        return future(() -> economy.depositPlayer(playerId, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(OfflinePlayer player, BigDecimal amount, String reason) {
        return future(() -> economy.depositPlayer(player, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(String playerName, String worldName, BigDecimal amount, String reason) {
        return future(() -> economy.depositPlayer(playerName, worldName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(UUID playerId, String worldName, BigDecimal amount, String reason) {
        return future(() -> economy.depositPlayer(playerId, worldName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> depositPlayer(OfflinePlayer player, String worldName, BigDecimal amount, String reason) {
        return future(() -> economy.depositPlayer(player, worldName, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> createBank(String name, String playerName) {
        return future(() -> economy.createBank(name, playerName).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> createBank(String name, UUID playerId) {
        return future(() -> economy.createBank(name, playerId).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> createBank(String name, OfflinePlayer player) {
        return future(() -> economy.createBank(name, player).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> deleteBank(String name) {
        return future(() -> economy.deleteBank(name).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> bankBalance(String name) {
        return future(() -> economy.bankBalance(name).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> bankHas(String name, BigDecimal amount) {
        return future(() -> economy.bankHas(name, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> bankWithdraw(String name, BigDecimal amount, String reason) {
        return future(() -> economy.bankWithdraw(name, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> bankDeposit(String name, BigDecimal amount, String reason) {
        return future(() -> economy.bankDeposit(name, amount.doubleValue()).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankOwner(String name, String playerName) {
        return future(() -> economy.isBankOwner(name, playerName).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankOwner(String name, UUID playerId) {
        return future(() -> economy.isBankOwner(name, playerId).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankOwner(String name, OfflinePlayer player) {
        return future(() -> economy.isBankOwner(name, player).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankMember(String name, String playerName) {
        return future(() -> economy.isBankMember(name, playerName).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankMember(String name, UUID playerId) {
        return future(() -> economy.isBankMember(name, playerId).toTresor());
    }

    @Override
    public CompletableFuture<EconomyResponse> isBankMember(String name, OfflinePlayer player) {
        return future(() -> economy.isBankMember(name, player).toTresor());
    }

    @Override
    public CompletableFuture<List<String>> getBanks() {
        return future(() -> economy.getBanks());
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(String playerName) {
        return future(() -> economy.createPlayerAccount(playerName));
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(UUID playerId) {
        return future(() -> economy.createPlayerAccount(playerId));
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(OfflinePlayer player) {
        return future(() -> economy.createPlayerAccount(player));
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(String playerName, String worldName) {
        return future(() -> economy.createPlayerAccount(playerName, worldName));
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(UUID playerId, String worldName) {
        return future(() -> economy.createPlayerAccount(playerId, worldName));
    }

    @Override
    public CompletableFuture<Boolean> createPlayerAccount(OfflinePlayer player, String worldName) {
        return future(() -> economy.createPlayerAccount(player, worldName));
    }

}

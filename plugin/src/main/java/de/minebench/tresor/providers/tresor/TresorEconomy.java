package de.minebench.tresor.providers.tresor;

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

import de.minebench.tresor.Provider;
import de.minebench.tresor.Tresor;
import de.minebench.tresor.services.economy.EconomyResponse;
import de.minebench.tresor.services.economy.ModernEconomy;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TresorEconomy extends Provider<Economy, Tresor> implements Economy, Listener {

    private ModernEconomy economy = null;

    public TresorEconomy() {
        super(Economy.class, ServicePriority.Lowest);
        updateProvider();
    }

    private void updateProvider() {
        RegisteredServiceProvider<ModernEconomy> provider = Bukkit.getServicesManager().getRegistration(ModernEconomy.class);
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

    private static <T> T get(CompletableFuture<T> future, T def) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return def;
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
    public boolean hasBankSupport() {
        return economy.supports(ModernEconomy.Feature.BANK);
    }

    @Override
    public int fractionalDigits() {
        return economy.fractionalDigits();
    }

    @Override
    public String format(double amount) {
        return economy.format(BigDecimal.valueOf(amount));
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
    public boolean hasAccount(String player) {
        return get(economy.hasAccount(player), false);
    }

    @Override
    public boolean hasAccount(UUID player) {
        return get(economy.hasAccount(player), false);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return get(economy.hasAccount(player), false);
    }

    @Override
    public boolean hasAccount(String player, String worldName) {
        return get(economy.hasAccount(player, worldName), false);
    }

    @Override
    public boolean hasAccount(UUID player, String worldName) {
        return get(economy.hasAccount(player, worldName), false);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return get(economy.hasAccount(player, worldName), false);
    }

    @Override
    public double getBalance(String player) {
        return get(economy.getBalance(player), BigDecimal.ZERO).doubleValue();
    }

    @Override
    public double getBalance(UUID player) {
        return get(economy.getBalance(player), BigDecimal.ZERO).doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return get(economy.getBalance(player), BigDecimal.ZERO).doubleValue();
    }

    @Override
    public double getBalance(String player, String world) {
        return get(economy.getBalance(player, world), BigDecimal.ZERO).doubleValue();
    }

    @Override
    public double getBalance(UUID player, String world) {
        return get(economy.getBalance(player, world), BigDecimal.ZERO).doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return get(economy.getBalance(player, world), BigDecimal.ZERO).doubleValue();
    }

    @Override
    public boolean has(String player, double amount) {
        return get(economy.has(player, BigDecimal.valueOf(amount)), false);
    }

    @Override
    public boolean has(UUID player, double amount) {
        return get(economy.has(player, BigDecimal.valueOf(amount)), false);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return get(economy.has(player, BigDecimal.valueOf(amount)), false);
    }

    @Override
    public boolean has(String player, String worldName, double amount) {
        return get(economy.has(player, worldName, BigDecimal.valueOf(amount)), false);
    }

    @Override
    public boolean has(UUID player, String worldName, double amount) {
        return get(economy.has(player, worldName, BigDecimal.valueOf(amount)), false);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return get(economy.has(player, worldName, BigDecimal.valueOf(amount)), false);
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse withdrawPlayer(String player, double amount) {
        return get(economy.withdrawPlayer(player, BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse withdrawPlayer(UUID player, double amount) {
        return get(economy.withdrawPlayer(player, BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return get(economy.withdrawPlayer(player, BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse withdrawPlayer(String player, String worldName, double amount) {
        return get(economy.withdrawPlayer(player, worldName,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse withdrawPlayer(UUID player, String worldName, double amount) {
        return get(economy.withdrawPlayer(player, worldName,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return get(economy.withdrawPlayer(player, worldName,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse depositPlayer(String player, double amount) {
        return get(economy.depositPlayer(player,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse depositPlayer(UUID player, double amount) {
        return get(economy.depositPlayer(player,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return get(economy.depositPlayer(player,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse depositPlayer(String player, String worldName, double amount) {
        return get(economy.depositPlayer(player, worldName,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse depositPlayer(UUID player, String worldName, double amount) {
        return get(economy.depositPlayer(player, worldName,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return get(economy.depositPlayer(player, worldName,BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.valueOf(amount),
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse createBank(String name, String player) {
        return get(economy.createBank(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse createBank(String name, UUID player) {
        return get(economy.createBank(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse createBank(String name, OfflinePlayer player) {
        return get(economy.createBank(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse deleteBank(String name) {
        return get(economy.deleteBank(name), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse bankBalance(String name) {
        return get(economy.bankBalance(name), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse bankHas(String name, double amount) {
        return get(economy.bankHas(name, BigDecimal.valueOf(amount)), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse bankWithdraw(String name, double amount) {
        return get(economy.bankWithdraw(name, BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse bankDeposit(String name, double amount) {
        return get(economy.bankDeposit(name, BigDecimal.valueOf(amount), "Vault"), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse isBankOwner(String name, String player) {
        return get(economy.isBankOwner(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse isBankOwner(String name, UUID player) {
        return get(economy.isBankOwner(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return get(economy.isBankOwner(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse isBankMember(String name, String player) {
        return get(economy.isBankMember(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse isBankMember(String name, UUID player) {
        return get(economy.isBankMember(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public net.milkbowl.vault.economy.EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return get(economy.isBankMember(name, player), new EconomyResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                EconomyResponse.ResponseType.FAILURE,
                "Exception occurred. Check logs."
        )).toVault();
    }

    @Override
    public List<String> getBanks() {
        return get(economy.getBanks(), new ArrayList<>());
    }

    @Override
    public boolean createPlayerAccount(String player) {
        return get(economy.createPlayerAccount(player), false);
    }

    @Override
    public boolean createPlayerAccount(UUID player) {
        return get(economy.createPlayerAccount(player), false);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return get(economy.createPlayerAccount(player), false);
    }

    @Override
    public boolean createPlayerAccount(String player, String worldName) {
        return get(economy.createPlayerAccount(player, worldName), false);
    }

    @Override
    public boolean createPlayerAccount(UUID player, String worldName) {
        return get(economy.createPlayerAccount(player, worldName), false);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return get(economy.createPlayerAccount(player, worldName), false);
    }
}

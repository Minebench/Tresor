package de.minebench.tresor.providers.tresor;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2020 Max Lee aka Phoenix616 (mail@moep.tv)
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
import lombok.experimental.Delegate;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class TresorEconomy extends Provider<Economy, Tresor> implements Economy, Listener {

    private interface Excludes {
        boolean isEnabled();
        String getName();
    }
    @Delegate(excludes = Excludes.class, types = {Economy.class})
    private TresorEconomy economy = null;

    public TresorEconomy() {
        super(Economy.class, ServicePriority.Lowest);
        updateProvider();
    }

    private void updateProvider() {
        RegisteredServiceProvider<TresorEconomy> provider = Bukkit.getServicesManager().getRegistration(TresorEconomy.class);
        if (provider != null && provider.getProvider() != this) {
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
}

package de.minebench.tresor;

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

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import de.minebench.tresor.services.authentication.Authentication;
import de.minebench.tresor.services.economy.ModernEconomy;
import de.themoep.hook.bukkit.HookManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class Tresor extends JavaPlugin implements TresorAPI {
    
    private WrappedServicesManager servicesManager;

    private Multimap<Class<?>, ProviderManager> hookManager = MultimapBuilder.hashKeys().arrayListValues().build();
    
    @Override
    public void onEnable() {
        getServer().getServicesManager().register(TresorAPI.class, this, this, ServicePriority.Normal);
        saveDefaultConfig();
        servicesManager = new WrappedServicesManager(this, getServer().getServicesManager());
        try {
            injectServicesManager();
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            getLogger().log(Level.SEVERE, "Error while trying to inject WrappedServicesManager! Service mapping will not work!" + e.getMessage());
        }
        getServer().getServicesManager().register(TresorServicesManager.class, servicesManager, this, ServicePriority.Normal);
        
        loadProviders();

        Metrics metrics = new Metrics(this, 17883);
        metrics.addCustomChart(new SimplePie("usesTresorServicesManager", () -> String.valueOf(getServer().getServicesManager() instanceof TresorServicesManager)));
    }
    
    private void injectServicesManager() throws IllegalAccessException, NoSuchFieldException, SecurityException {
        Field field = getServer().getClass().getDeclaredField("servicesManager");
        field.setAccessible(true);
        field.set(getServer(), servicesManager);
    }
    
    private void loadProviders() {
        new ProviderManager(Authentication.class);
        new ProviderManager(Economy.class);
        new ProviderManager(ModernEconomy.class);
        new ProviderManager(Permission.class);
        new ProviderManager(Chat.class);
    }

    @Override
    public TresorServicesManager getServicesManager() {
        return servicesManager;
    }
    
    /**
     * Get the name of the service provider for a plugin
     * @param service   The service to get the provider name for
     * @param plugin    The plugin to get the name for
     * @return The service name or null if there was none configured
     */
    public String getProviderName(Class<?> service, JavaPlugin plugin) {
        return getConfig().getString("providers." + service.getSimpleName().toLowerCase() + "." + plugin.getName(),
                getConfig().getString("providers." + service.getSimpleName().toLowerCase() + ".default", null));
    }

    private class ProviderManager extends HookManager {

        public ProviderManager(Class<?> provider) {
            super(Tresor.this, "de.minebench.tresor.providers", true);
            setSuffix(provider.getSimpleName());
        }
    }
}

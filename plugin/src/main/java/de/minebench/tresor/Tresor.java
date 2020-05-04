package de.minebench.tresor;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2020 Max Lee (https://github.com/Phoenix616)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

public class Tresor extends JavaPlugin {
    
    private WrappedServicesManager servicesManager;
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        servicesManager = new WrappedServicesManager(this, getServer().getServicesManager());
        try {
            injectServicesManager();
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            getLogger().log(Level.SEVERE, "Error while trying to inject WrappedServicesManager! Service mapping will not work!" + e.getMessage());
        }
        
        loadProviders();
        
        try {
            org.mcstats.MetricsLite metrics = new org.mcstats.MetricsLite(this);
            metrics.start();
        } catch(IOException e) {
            // metrics failed to load
        }
        
        new org.bstats.MetricsLite(this);
    }
    
    private void injectServicesManager() throws IllegalAccessException, NoSuchFieldException, SecurityException {
        Field field = getServer().getClass().getDeclaredField("servicesManager");
        field.setAccessible(true);
        field.set(getServer(), servicesManager);
    }
    
    private void loadProviders() {
    
    }
    
    /**
     * Get the TresorServicesManager
     * @return The TresorServicesManager instance
     */
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
}

package de.minebench.tresor;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2023 Max Lee aka Phoenix616 (max@themoep.de)
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

import de.minebench.tresor.services.TresorServiceProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hook into a service registered with the {@link org.bukkit.plugin.ServicesManager}
 * @param <S> The service to hook into
 */
public class ServiceHook<S> {
    private final JavaPlugin plugin;
    private final Class<S> serviceClass;
    private final TresorServicesManager serviceManager;
    private S serviceProvider = null;

    public ServiceHook(JavaPlugin plugin, Class<S> serviceClass) {
        this.plugin = plugin;
        this.serviceClass = serviceClass;
        // Use the bukkit service manager to hook into tresor as our own might not exist yet!
        serviceManager = plugin.getServer().getServicesManager().load(TresorServicesManager.class);
        if (serviceManager == null) {
            // This should not be possible
            throw new IllegalStateException("TresorServicesManager provider wasn't found?");
        }
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onServiceRegister(ServiceRegisterEvent event) {
                if (event.getProvider().getService() == serviceClass) {
                    updateServiceProvider();
                }
            }

            @EventHandler
            public void onServiceUnregister(ServiceUnregisterEvent event) {
                if (event.getProvider().getService() == serviceClass) {
                    updateServiceProvider();
                }
            }
        }, plugin);
        updateServiceProvider();
    }

    private void updateServiceProvider() {
        RegisteredServiceProvider<S> rsp = serviceManager.getRegistration(plugin, serviceClass);

        if (rsp != null) {
            serviceProvider = rsp.getProvider();
            plugin.getLogger().info("Using " + (serviceProvider instanceof TresorServiceProvider
                    ? ((TresorServiceProvider) serviceProvider).getName() : rsp.getPlugin().getName())
                    + " as the " + serviceClass.getSimpleName() + " provider now.");
        }
    }

    /**
     * Get the provider, might be null!
     * @return The provider to hook into.
     */
    public S getProvider() {
        return serviceProvider;
    }
}

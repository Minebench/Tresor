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

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public interface TresorServicesManager extends ServicesManager {
    
    /**
     * Queries for a provider registration that corresponds to the plugin calling the method or
     * a default one if it wasn't mapped. This may return if no provider has been registered for a service.
     * @param service   The service interface
     * @param <T>       The service interface
     * @return Provider registration or null if non was found or the plugin was configured to not have one
     * @deprecated Use {@link #getRegistration(JavaPlugin, Class)}
     */
    @Deprecated
    @Override
    <T> RegisteredServiceProvider<T> getRegistration(Class<T> service);
    
    /**
     * Queries for a provider registration that corresponds to the plugin or a default one if it wasn't mapped.
     * This may return if no provider has been registered for a service.
     * @param plugin    The plugin to get the registration for
     * @param service   The service interface
     * @param <T>       The service interface
     * @return Provider registration or null if non was found or the plugin was configured to not have one
     */
    <T> RegisteredServiceProvider<T> getRegistration(JavaPlugin plugin, Class<T> service);
    
    /**
     * Get registrations of providers for a service. With the services
     * corresponding to the calling plugin as the first element.
     * The returned list is unmodifiable.
     * @param service   The service interface
     * @param <T>       The service interface
     * @return List of registrations
     * @deprecated Use {@link #getRegistrations(JavaPlugin, Class)}
     */
    @Deprecated
    @Override
    <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> service);
    
    /**
     * Get registrations of providers for a service. With the services
     * corresponding to the plugin as the first element.
     * The returned list is unmodifiable.
     * @param plugin    The plugin to get the registration for
     * @param service   The service interface
     * @param <T>       The service interface
     * @return List of registrations
     */
    <T> Collection<RegisteredServiceProvider<T>> getRegistrations(JavaPlugin plugin, Class<T> service);
    
    /**
     * Get a list of known services. A service is known if it has registered
     * providers for it and if it wasn't ignored for the calling plugin.
     * @return List of known services
     * @deprecated Use {@link #getKnownServices(JavaPlugin)}
     */
    @Deprecated
    @Override
    Collection<Class<?>> getKnownServices();
    
    /**
     * Get a list of known services. A service is known if it has registered
     * providers for it and if it wasn't ignored for the plugin.
     * @param plugin The plugin to get the services for
     * @return List of known services
     */
    Collection<Class<?>> getKnownServices(JavaPlugin plugin);
    
    /**
     * Returns whether a provider has been registered for a service and the
     * calling plugin. Do not check this first only to call <code>load(service)</code>
     * later, as that would be a non-thread safe situation.
     * @param service   The service interface to check
     * @param <T>       The service interface
     * @return whether there has been a registered provider
     * @deprecated Use {@link #isProvidedFor(JavaPlugin, Class)}
     */
    @Deprecated
    @Override
    <T> boolean isProvidedFor(Class<T> service);
    
    /**
     * Returns whether a provider has been registered for a service and the plugin.
     * Do not check this first only to call <code>load(service)</code> later, as
     * that would be a non-thread safe situation.
     * @param plugin    The plugin to get the services for
     * @param service   The service interface to check
     * @param <T>       The service interface
     * @return whether there has been a registered provider
     */
    <T> boolean isProvidedFor(JavaPlugin plugin, Class<T> service);

}

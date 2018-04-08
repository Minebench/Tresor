package de.minebench.tresor;

import org.bukkit.Bukkit;
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
     */
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
     */
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
     */
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
     */
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
    
    public static TresorServicesManager get() {
        if (Bukkit.getServer().getServicesManager() instanceof TresorServicesManager) {
            return (TresorServicesManager) Bukkit.getServer().getServicesManager();
        }
        return null;
    }
}

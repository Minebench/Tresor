package de.minebench.tresor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.experimental.Delegate;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.logging.Level;

public class WrappedServicesManager implements TresorServicesManager {
    private final Tresor tresor;
    private interface Excludes {
        <T> RegisteredServiceProvider<T> getRegistration(Class<T> service);
        <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> service);
        Collection<Class<?>> getKnownServices();
        <T> boolean isProvidedFor(Class<T> var1);
    }
    @Delegate(excludes = Excludes.class, types = {ServicesManager.class})
    private final ServicesManager parent;
    
    public WrappedServicesManager(Tresor plugin, ServicesManager parent) {
        tresor = plugin;
        this.parent = parent;
    }
    
    private JavaPlugin getCallingPlugin() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            if (!element.getClassName().equals(WrappedServicesManager.class.getName())
                    && !element.getClassName().startsWith("org.bukkit.")
                    && !element.getClassName().startsWith("org.spigotmc.")
                    && !element.getClassName().startsWith("java.")) {
                try {
                    Class clazz = Class.forName(element.getClassName());
                    return JavaPlugin.getProvidingPlugin(clazz);
                } catch (ClassNotFoundException | IllegalArgumentException | IllegalStateException e) {
                    tresor.getLogger().log(Level.WARNING, "Could not get plugin of " + element.getClassName() + "! " + e.getMessage());
                }
            }
        }
        return null;
    }
    
    @Override
    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service) {
        JavaPlugin plugin = getCallingPlugin();
        return plugin != null ? getRegistration(plugin, service) : parent.getRegistration(service);
    }
    
    @Override
    public <T> RegisteredServiceProvider<T> getRegistration(JavaPlugin plugin, Class<T> service) {
        String providerName = tresor.getProviderName(service, plugin);
        if (providerName != null) {
            if (providerName.isEmpty()) {
                return null;
            }
            for (RegisteredServiceProvider<T> provider : parent.getRegistrations(service)) {
                if (provider.getPlugin().getName().equalsIgnoreCase(providerName)) {
                    return provider;
                }
            }
            tresor.getLogger().log(Level.WARNING, "Plugin " + providerName + " was configured for " + plugin.getName() + " but it wasn't found?");
            return null;
        }
        return parent.getRegistration(service);
    }
    
    @Override
    public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> service) {
        JavaPlugin plugin = getCallingPlugin();
        return plugin != null ? getRegistrations(plugin, service) : parent.getRegistrations(service);
    }
    
    @Override
    public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(JavaPlugin plugin, Class<T> service) {
        ImmutableList.Builder<RegisteredServiceProvider<T>> ret = ImmutableList.builder();
        
        RegisteredServiceProvider<T> pluginProvider = getRegistration(plugin, service);
        if (pluginProvider != null) {
            ret.add(pluginProvider);
        }
        
        for (RegisteredServiceProvider<T> provider : getRegistrations(service)) {
            if (provider != pluginProvider) {
                ret.add(provider);
            }
        }
        
        return ret.build();
    }
    
    @Override
    public Collection<Class<?>> getKnownServices() {
        JavaPlugin plugin = getCallingPlugin();
        return plugin != null ? getKnownServices(plugin) : parent.getKnownServices();
    }
    
    @Override
    public Collection<Class<?>> getKnownServices(JavaPlugin plugin) {
        ImmutableSet.Builder<Class<?>> ret = ImmutableSet.builder();
        for (Class<?> service : parent.getKnownServices()) {
            String providerName = tresor.getProviderName(service, plugin);
            if (providerName == null || !providerName.isEmpty()) {
                ret.add(service);
            }
        }
        return ret.build();
    }
    
    @Override
    public <T> boolean isProvidedFor(Class<T> service) {
        JavaPlugin plugin = getCallingPlugin();
        return plugin != null ? isProvidedFor(plugin, service) : parent.isProvidedFor(service);
    }
    
    @Override
    public <T> boolean isProvidedFor(JavaPlugin plugin, Class<T> service) {
        return getKnownServices(plugin).contains(service);
    }
}

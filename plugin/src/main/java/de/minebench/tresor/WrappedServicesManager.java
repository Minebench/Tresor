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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public class WrappedServicesManager implements TresorServicesManager {
    private final Tresor tresor;
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
    public <T> void register(Class<T> service, T provider, Plugin plugin, ServicePriority priority) {
        parent.register(service, provider, plugin, priority);
    }

    @Override
    public void unregisterAll(Plugin plugin) {
        parent.unregisterAll(plugin);
    }

    @Override
    public void unregister(Class<?> service, Object provider) {
        parent.unregister(service, provider);
    }

    @Override
    public void unregister(Object provider) {
        parent.unregister(provider);
    }

    @Override
    public <T> T load(Class<T> service) {
        return parent.load(service);
    }

    @Override
    public <T> T load(JavaPlugin plugin, Class<T> service) {
        RegisteredServiceProvider<T> provider = getRegistration(plugin, service);
        if (provider != null) {
            return provider.getProvider();
        }
        return parent.load(service);
    }

    @Override
    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service) {
        JavaPlugin plugin = getCallingPlugin();
        return plugin != null ? getRegistration(plugin, service) : parent.getRegistration(service);
    }

    @Override
    public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin) {
        return parent.getRegistrations(plugin);
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

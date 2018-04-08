package de.minebench.tresor;

import lombok.experimental.Delegate;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class WrappedServicesManager implements TresorServicesManager {
    private final Tresor tresor;
    private interface Excludes {
        <T> RegisteredServiceProvider<T> getRegistration(Class<T> service);
    }
    @Delegate(excludes = Excludes.class, types = {ServicesManager.class})
    private final ServicesManager parent;
    
    public WrappedServicesManager(Tresor plugin, ServicesManager parent) {
        tresor = plugin;
        this.parent = parent;
    }
    
    @Override
    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service) {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            if (!element.getClassName().equals(WrappedServicesManager.class.getName())
                    && !element.getClassName().startsWith("org.bukkit.")
                    && !element.getClassName().startsWith("org.spigotmc.")
                    && !element.getClassName().startsWith("java.")) {
                try {
                    Class clazz = Class.forName(element.getClassName());
                    JavaPlugin plugin = JavaPlugin.getProvidingPlugin(clazz);
                    return getRegistration(plugin, service);
                } catch (ClassNotFoundException | IllegalArgumentException | IllegalStateException e) {
                    tresor.getLogger().log(Level.WARNING, "Could not get plugin of " + element.getClassName() + "! " + e.getMessage());
                }
            }
        }
        return parent.getRegistration(service);
    }
    
    @Override
    public <T> RegisteredServiceProvider<T> getRegistration(JavaPlugin plugin, Class<T> service) {
        String serviceName = tresor.getConfig().getString("services." + service.getSimpleName().toLowerCase() + "." + plugin.getName(),
                tresor.getConfig().getString("services." + service.getSimpleName().toLowerCase() + ".default", null));
        if (serviceName != null) {
            for (RegisteredServiceProvider<T> provider : parent.getRegistrations(service)) {
                if (provider.getPlugin().getName().equalsIgnoreCase(serviceName)) {
                    return provider;
                }
            }
            tresor.getLogger().log(Level.WARNING, "Plugin " + serviceName + " was configured for " + plugin.getName() + " but it wasn't found?");
            return null;
        }
        return parent.getRegistration(service);
    }
}

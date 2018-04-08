package de.minebench.tresor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public interface TresorServicesManager extends ServicesManager {
    
    <T> RegisteredServiceProvider<T> getRegistration(JavaPlugin plugin, Class<T> service);
    
    public static TresorServicesManager get() {
        if (Bukkit.getServer().getServicesManager() instanceof TresorServicesManager) {
            return (TresorServicesManager) Bukkit.getServer().getServicesManager();
        }
        return null;
    }
}

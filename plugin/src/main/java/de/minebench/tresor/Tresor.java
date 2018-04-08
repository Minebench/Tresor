package de.minebench.tresor;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;

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
    
    public TresorServicesManager getServicesManager() {
        return servicesManager;
    }
}

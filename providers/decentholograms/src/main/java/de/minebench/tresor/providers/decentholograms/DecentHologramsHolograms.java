package de.minebench.tresor.providers.decentholograms;

import de.minebench.tresor.Provider;
import de.minebench.tresor.services.hologram.Hologram;
import de.minebench.tresor.services.hologram.Holograms;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.plugin.DecentHologramsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DecentHologramsHolograms extends Provider<Holograms, DecentHologramsPlugin> implements Holograms {

    private DecentHologramsPlugin hooked;

    public DecentHologramsHolograms() {
        super(Holograms.class);
    }

    @Override
    public Hologram createHologram(String hologramId, Location location) {
        return new DecentHologramWrapper(DHAPI.createHologram(hologramId, location));
    }

    @Override
    public Hologram getHologram(String hologramId) {
        eu.decentsoftware.holograms.api.holograms.Hologram hologram = DHAPI.getHologram(hologramId);

        if (hologram == null) {
            return null;
        }

        return new DecentHologramWrapper(hologram);
    }

    @Override
    public DecentHologramsPlugin getHooked() {
        if (hooked == null) {
            hooked = (DecentHologramsPlugin) Bukkit.getPluginManager().getPlugin("DecentHolograms");
        }

        return hooked;
    }

    @Override
    public boolean isEnabled() {
        return getHooked() != null && getHooked().isEnabled();
    }

    @Override
    public String getName() {
        return "DecentHolograms";
    }
}

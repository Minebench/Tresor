package de.minebench.tresor.providers.decentholograms;

import de.minebench.tresor.Provider;
import de.minebench.tresor.services.hologram.Hologram;
import de.minebench.tresor.services.hologram.HologramProvider;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.plugin.DecentHologramsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DecentHologramsProvider extends Provider<HologramProvider, DecentHologramsPlugin> implements HologramProvider {

    private DecentHologramsPlugin hooked;

    public DecentHologramsProvider() {
        super(HologramProvider.class);
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
    public String getName() {
        return "DecentHolograms";
    }
}

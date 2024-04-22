package de.minebench.tresor.services.hologram;

import org.bukkit.Location;

public interface HologramProvider {

    Hologram createHologram(String hologramId, Location location);
    Hologram getHologram(String hologramId);
}

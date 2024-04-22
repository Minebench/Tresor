package de.minebench.tresor.services.hologram;

import de.minebench.tresor.services.TresorServiceProvider;
import org.bukkit.Location;

public interface Holograms extends TresorServiceProvider {

    Hologram createHologram(String hologramId, Location location);
    Hologram getHologram(String hologramId);
}

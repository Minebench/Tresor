package de.minebench.tresor.services.hologram;

import de.minebench.tresor.services.hologram.Holograms.Feature;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Hologram {

    Holograms getProvider();
    String getHologramId();

    void addLine(String line);
    void setLine(int index, String line);
    void removeLine(int index);
    String getLine(int index);

    double getLineHeight();
    void setLineHeight(double height);

    Location getLocation();
    void teleport(Location location);

    void showTo(Player player);
    void hideFrom(Player player);

    void destroy();

    default boolean supports(Feature feature) {
        return getProvider().supports(feature);
    }

}

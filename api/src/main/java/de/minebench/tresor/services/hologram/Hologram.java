package de.minebench.tresor.services.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Hologram {

    double LINE_HEIGHT = 0.25;

    String getHologramId();

    void addLine(String line);
    void setLine(int index, String line);
    void removeLine(int index);
    String getLine(int index);

    Location getLocation();
    void teleport(Location location);

    void showTo(Player player);
    void hideFrom(Player player);

    void destroy();
}

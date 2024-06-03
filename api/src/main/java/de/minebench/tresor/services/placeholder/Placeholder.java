package de.minebench.tresor.services.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface Placeholder {

    /**
     * Get the key of the placeholder
     * @return The key
     */
    String getKey();

    /**
     * Request the value of the placeholder
     * @param player The player to request the value for
     * @return The value
     */
    String request(final OfflinePlayer player);
}

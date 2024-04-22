package de.minebench.tresor.services.hologram;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2024 Max Lee aka Phoenix616 (max@themoep.de)
 * Copyright (C) 2024 Tresor Contributors (https://github.com/Minebench/Tresor/graphs/contributors)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.minebench.tresor.services.hologram.Holograms.Feature;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A hologram provided by a {@link Holograms} service provider
 */
public interface Hologram {

    /**
     * Get the provider that created this hologram
     * @return The provider
     */
    Holograms getProvider();

    /**
     * Get the id of the hologram
     * @return The id
     */
    String getHologramId();

    /**
     * Add a line to the hologram
     * @param line The line to add
     */
    void addLine(String line);

    /**
     * Set a line at the given index
     * @param index The index to set the line at
     * @param line  The line to set
     */
    void setLine(int index, String line);

    /**
     * Remove a line at the given index
     * @param index The index to remove the line at
     */
    void removeLine(int index);

    /**
     * Get the line at the given index
     * @param index The index to get the line at
     * @return The line
     */
    String getLine(int index);

    /**
     * Get the height of the individual lines in this hologram
     * @return The height, 1 = the height of a standard display tag
     */
    double getLineHeight();

    /**
     * Set the height of the individual lines in this hologram
     * @param height The height, 1 = the height of a standard display tag
     */
    void setLineHeight(double height);

    /**
     * Get the location of the hologram
     * @return The location in the world
     */
    Location getLocation();

    /**
     * Teleport the hologram to a new location
     * @param location The new location
     */
    void teleport(Location location);

    /**
     * Show the hologram to a player. Only if it supports {@link Feature#PER_PLAYER}. Check with {@link #supports(Feature)}.
     * @param player The player to show the hologram to
     * @throws UnsupportedOperationException if the hologram does not support {@link Feature#PER_PLAYER}
     */
    void showTo(Player player);

    /**
     * Hide the hologram from a player. Only if it supports {@link Feature#PER_PLAYER}. Check with {@link #supports(Feature)}.
     * @param player The player to hide the hologram from
     * @throws UnsupportedOperationException if the hologram does not support {@link Feature#PER_PLAYER}
     */
    void hideFrom(Player player);

    /**
     * Destroy the hologram and remove it from the world
     */
    void destroy();

    /**
     * Check if the provider supports a certain feature
     * @param feature The feature to check for
     * @return Whether the feature is supported
     */
    default boolean supports(Feature feature) {
        return getProvider().supports(feature);
    }

}

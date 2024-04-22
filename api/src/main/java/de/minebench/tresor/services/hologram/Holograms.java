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

import de.minebench.tresor.services.TresorServiceProvider;
import org.bukkit.Location;

/**
 * The holograms service API for managing {@link Hologram}s

 */
public interface Holograms extends TresorServiceProvider {

    /**
     * Create a hologram at the given location
     * @param hologramId The id of the hologram
     * @param location   The location to create the hologram at
     * @return The created hologram
     */
    Hologram createHologram(String hologramId, Location location);

    /**
     * Get a hologram by its id
     * @param hologramId The id of the hologram
     * @return The hologram or null if it doesn't exist
     */
    Hologram getHologram(String hologramId);

    /**
     * Check if the provider supports a certain feature
     * @param feature The feature to check for
     * @return Whether the feature is supported
     */
    boolean supports(Feature feature);

    /**
     * The features that can be supported by a hologram provider
     */
    enum Feature {
        /**
         * Holograms can be toggled per player
         */
        PER_PLAYER,
    }
}

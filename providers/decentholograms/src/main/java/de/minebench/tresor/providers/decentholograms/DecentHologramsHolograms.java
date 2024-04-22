package de.minebench.tresor.providers.decentholograms;

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
        return new DecentHologramWrapper(this, DHAPI.createHologram(hologramId, location));
    }

    @Override
    public Hologram getHologram(String hologramId) {
        eu.decentsoftware.holograms.api.holograms.Hologram hologram = DHAPI.getHologram(hologramId);

        if (hologram == null) {
            return null;
        }

        return new DecentHologramWrapper(this, hologram);
    }

    @Override
    public boolean supports(Feature feature) {
        if (feature == Feature.PER_PLAYER) {
            return true;
        }

        return false;
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

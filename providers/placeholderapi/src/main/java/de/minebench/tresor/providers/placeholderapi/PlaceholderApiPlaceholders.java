package de.minebench.tresor.providers.placeholderapi;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2024 Max Lee aka Phoenix616 (max@themoep.de)
 * Copyright (C) 2024 Tresor Contributors (https://github.com/Minebench/Tresor/graphs/contributors)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
 */


import org.bukkit.Bukkit;

import de.minebench.tresor.Provider;
import de.minebench.tresor.services.placeholders.Placeholders;
import de.minebench.tresor.services.placeholders.TresorPlaceholderExpansion;
import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class PlaceholderApiPlaceholders extends Provider<Placeholders, PlaceholderAPIPlugin> implements Placeholders {

    private PlaceholderAPIPlugin hooked;

    public PlaceholderApiPlaceholders() {
        super(Placeholders.class);
    }

    /*
     * Tresor Hook related methods
     */

     @Override
    public boolean isEnabled() {
        return getHooked() != null ? hooked.isEnabled() : Bukkit.getServer().getPluginManager().isPluginEnabled(getName());
    }

    @Override
    public PlaceholderAPIPlugin getHooked() {
        if (hooked == null) {
            hooked = (PlaceholderAPIPlugin) Bukkit.getServer().getPluginManager().getPlugin(getName());
        }
        return hooked;
    }
    
    @Override
    public String getName() {
        return "PlaceholderAPI";
    }

    /*
     * Placeholder Expansion related methods
     */

    @Override
    public boolean registerTresorPlaceholderExpansion(TresorPlaceholderExpansion placeholderExpansion) {
        PlaceholderApiExpansionWrapper expansionWrapper = new PlaceholderApiExpansionWrapper(placeholderExpansion);
        return expansionWrapper.register();
    }



}

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


import org.bukkit.OfflinePlayer;

import de.minebench.tresor.services.placeholders.TresorPlaceholderExpansion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
public class PlaceholderApiExpansionWrapper extends PlaceholderExpansion {

    private TresorPlaceholderExpansion TresorPlaceholderExpansion;

    public PlaceholderApiExpansionWrapper(TresorPlaceholderExpansion TresorPlaceholderExpansion) throws NullPointerException {
        if(TresorPlaceholderExpansion == null) {
            throw new NullPointerException("Cannot construct PlaceholderApiExpansionWrapper from null TresorPlaceholderExpansion");
        }
        this.TresorPlaceholderExpansion = TresorPlaceholderExpansion;
    }

    @Override
    public String getIdentifier() {
        return TresorPlaceholderExpansion.getIdentifier();
    }

    @Override
    public String getAuthor() {
        return TresorPlaceholderExpansion.getAuthor();
    }

    @Override
    public String getRequiredPlugin() {
        return TresorPlaceholderExpansion.getRequiredPlugin();
    }

    @Override
    public String getVersion() {
        return TresorPlaceholderExpansion.getVersion();
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        return TresorPlaceholderExpansion.onRequest(player, params);
    }

}
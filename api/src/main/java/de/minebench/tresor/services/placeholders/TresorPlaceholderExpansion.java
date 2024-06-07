package de.minebench.tresor.services.placeholders;

/*
 * Tresor - Abstraction library for Bukkit plugins Copyright (C) 2024 Max Lee aka Phoenix616
 * (max@themoep.de) Copyright (C) 2024 Tresor Contributors
 * (https://github.com/Minebench/Tresor/graphs/contributors)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tresor. If not, see
 * <http://www.gnu.org/licenses/>.
 */


import org.bukkit.OfflinePlayer;

/**
 * The TresorPlaceholderExpansion class contains basic info about a plugins placeholders and a
 * method, onRequest, for developers to override with their placeholder implementations
 * 
 * *This class is obviously very largely from PlaceholderAPI's implementation
 */
public interface TresorPlaceholderExpansion {


    /**
     * An identifier so that all placeholders with this identifier are directed to this particular
     * implementation
     */
    public String getIdentifier();

    /**
     * The name of the plugin which this placeholder is dependent on to be able to function
     */
    public String getRequiredPlugin();

    public String getAuthor();

    public String getVersion();

    /**
     * 
     * @return Whether the requiredPlugin is present
     */
    public boolean canPlaceholderRegister();

    /**
     * Processes the params received for the specified player, returns the translated placeholder
     * 
     * @param player OfflinePlayer to translate params for
     * @param params The placeholder request without the identifier
     * @return Translated string
     */
    public abstract String onRequest(OfflinePlayer player, String params);

}

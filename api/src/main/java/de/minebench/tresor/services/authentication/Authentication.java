package de.minebench.tresor.services.authentication;

import de.minebench.tresor.services.TresorServiceProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2021 Max Lee aka Phoenix616 (max@themoep.de)
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

/**
 * The authentication service API for checking if a player was authenticated in another plugin.
 * Returns {@link java.util.concurrent.CompletableFuture}s to allow async usage if supported by the providing plugin.
 */
public interface Authentication extends TresorServiceProvider {

    /**
     * Returns true if the given implementation supports a given feature.
     *
     * @param feature The feature to check for
     * @return true if the implementation supports the feature
     */
    boolean supports(Feature feature);

    /**
     * Register what should happen on authentication
     *
     * @param plugin The plugin registering the listener
     * @param onAuth What should happen when a player authenticates. The boolean indicates whether this is an async event.
     */
    void registerAuthListener(Plugin plugin, BiConsumer<Player, Boolean> onAuth);

    /**
     * Remove all authentication listeners of a plugin
     *
     * @param plugin The plugin to remove the listeners from
     * @return The amount of listeners removed
     */
    int removeAuthListeners(Plugin plugin);

    /**
     * Returns whether a player is registered
     *
     * @param player The player to check
     * @return Whether the player is registered
     */
    CompletableFuture<Boolean> isRegistered(OfflinePlayer player);

    /**
     * Returns whether a player is registered
     *
     * @param playerName The name of the player to check
     * @return Whether the player is registered
     */
    CompletableFuture<Boolean> isRegistered(String playerName);

    /**
     * Returns whether a player is registered
     *
     * @param playerId The UUID of the player to check
     * @return Whether the player is registered
     */
    CompletableFuture<Boolean> isRegistered(UUID playerId);

    /**
     * Returns whether a player is currently considered authenticated
     *
     * @param player The player to check
     * @return Whether the player is currently considered authenticated
     */
    CompletableFuture<Boolean> isAuthenticated(Player player);

    enum Feature {
        ASYNC,
        EVENT
    }
}

package de.minebench.tresor.providers.nlogin;

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

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.nickuc.login.api.events.AsyncAuthenticateEvent;
import com.nickuc.login.api.nLoginAPI;
import de.minebench.tresor.Provider;
import de.minebench.tresor.services.authentication.Authentication;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static de.minebench.tresor.TresorUtils.asyncFuture;
import static de.minebench.tresor.TresorUtils.future;

public class nLoginAuthentication extends Provider<Authentication, Plugin> implements Authentication {

    private Plugin hooked;
    private Multimap<String, BiConsumer<Player, Boolean>> listeners = MultimapBuilder.hashKeys().arrayListValues().build();

    public nLoginAuthentication() {
        super(Authentication.class);
    }

    @Override
    public boolean isEnabled() {
        return nLoginAPI.getApi().isAvailable();
    }

    @Override
    public Plugin getHooked() {
        if (hooked == null) {
            hooked = Bukkit.getServer().getPluginManager().getPlugin(getName());
        }
        return hooked;
    }

    @Override
    public String getName() {
        return "nLogin";
    }

    @Override
    public boolean supports(Feature feature) {
        switch (feature) {
            case ASYNC:
            case EVENT:
                return true;
        }
        return false;
    }

    @Override
    public void registerAuthListener(Plugin plugin, BiConsumer<Player, Boolean> onAuth) {
        listeners.put(plugin.getName(), onAuth);
    }

    @Override
    public int removeAuthListeners(Plugin plugin) {
        return listeners.removeAll(plugin.getName()).size();
    }

    @EventHandler
    public void onAuth(AsyncAuthenticateEvent event) {
        for (BiConsumer<Player, Boolean> listener : listeners.values()) {
            listener.accept(event.getPlayer(), event.isAsynchronous());
        }
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(OfflinePlayer player) {
        if (player instanceof Player) {
            return asyncFuture(() -> nLoginAPI.getApi().isRegistered((Player) player));
        } else if (player.getName() != null) {
            return asyncFuture(() -> nLoginAPI.getApi().isRegistered(player.getName()));
        }
        return future(() -> false);
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(String playerName) {
        Validate.notNull(playerName, "Player name cannot be null!");
        return asyncFuture(() -> nLoginAPI.getApi().isRegistered(playerName));
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID playerId) {
        OfflinePlayer player = hooked.getServer().getOfflinePlayer(playerId);
        return isRegistered(player);
    }

    @Override
    public CompletableFuture<Boolean> isAuthenticated(Player player) {
        return future(() -> nLoginAPI.getApi().isAuthenticated(player));
    }
}

package de.minebench.tresor.providers.authme;

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
import de.minebench.tresor.Provider;
import de.minebench.tresor.services.authentication.Authentication;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static de.minebench.tresor.TresorUtils.asyncFuture;
import static de.minebench.tresor.TresorUtils.future;

public class AuthMeAuthentication extends Provider<Authentication, AuthMe> implements Authentication, Listener {

    private AuthMe hooked;
    private AuthMeApi api;
    private Multimap<String, BiConsumer<Player, Boolean>> listeners = MultimapBuilder.hashKeys().arrayListValues().build();

    public AuthMeAuthentication() {
        super(Authentication.class);
        api = AuthMeApi.getInstance();
    }

    @Override
    public void register() {
        super.register();
        Bukkit.getServer().getPluginManager().registerEvents(this, getHooked());
    }

    @Override
    public boolean isEnabled() {
        return getHooked() != null ? hooked.isEnabled() : Bukkit.getServer().getPluginManager().isPluginEnabled(getName());
    }

    @Override
    public AuthMe getHooked() {
        if (hooked == null) {
            hooked = (AuthMe) Bukkit.getServer().getPluginManager().getPlugin(getName());
        }
        return hooked;
    }

    @Override
    public String getName() {
        return "AuthMe";
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
    public void onAuth(LoginEvent event) {
        for (BiConsumer<Player, Boolean> listener : listeners.values()) {
            listener.accept(event.getPlayer(), event.isAsynchronous());
        }
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(OfflinePlayer player) {
        return isRegistered(player.getName());
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(String playerName) {
        return asyncFuture(() -> api.isRegistered(playerName));
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID playerId) {
        OfflinePlayer player = getHooked().getServer().getOfflinePlayer(playerId);
        return asyncFuture(() -> player.getName() != null && api.isRegistered(player.getName()));
    }

    @Override
    public CompletableFuture<Boolean> isAuthenticated(Player player) {
        return future(() -> api.isAuthenticated(player));
    }
}

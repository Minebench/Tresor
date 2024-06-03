package de.minebench.tresor.providers.openlogin;

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

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.nickuc.openlogin.bukkit.OpenLoginBukkit;
import com.nickuc.openlogin.bukkit.api.events.AsyncAuthenticateEvent;
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

public class OpeNLoginAuthentication extends Provider<Authentication, OpenLoginBukkit> implements Authentication {

    private OpenLoginBukkit hooked;
    private Multimap<String, BiConsumer<Player, Boolean>> listeners = MultimapBuilder.hashKeys().arrayListValues().build();

    public OpeNLoginAuthentication() {
        super(Authentication.class);
    }

    @Override
    public boolean isEnabled() {
        return getHooked() != null ? hooked.isEnabled() : Bukkit.getServer().getPluginManager().isPluginEnabled(getName());
    }

    @Override
    public OpenLoginBukkit getHooked() {
        if (hooked == null) {
            hooked = (OpenLoginBukkit) Bukkit.getServer().getPluginManager().getPlugin(getName());
        }
        return hooked;
    }

    @Override
    public String getName() {
        return "OpeNLogin";
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
        if (player.getName() != null) {
            return asyncFuture(() -> OpenLoginBukkit.getApi().isRegistered(player.getName()));
        }
        return future(() -> false);
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(String playerName) {
        Validate.notNull(playerName, "Player name cannot be null!");
        return asyncFuture(() -> OpenLoginBukkit.getApi().isRegistered(playerName));
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID playerId) {
        OfflinePlayer player = hooked.getServer().getOfflinePlayer(playerId);
        return asyncFuture(() -> player.getName() != null && OpenLoginBukkit.getApi().isRegistered(player.getName()));
    }

    @Override
    public CompletableFuture<Boolean> isAuthenticated(Player player) {
        return future(() -> hooked.getLoginManagement().isAuthenticated(player.getName()));
    }
}

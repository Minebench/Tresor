package de.minebench.tresor.providers.librelogin;

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
import de.minebench.tresor.Provider;
import de.minebench.tresor.services.authentication.Authentication;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.kyngs.librelogin.api.LibreLoginPlugin;
import xyz.kyngs.librelogin.api.database.User;
import xyz.kyngs.librelogin.api.event.events.AuthenticatedEvent;
import xyz.kyngs.librelogin.api.provider.LibreLoginProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static de.minebench.tresor.TresorUtils.asyncFuture;
import static de.minebench.tresor.TresorUtils.future;

public class LibreLoginAuthentication extends Provider<Authentication, Plugin> implements Authentication {

    private LibreLoginPlugin<Player, World> libreLogin;
    private Plugin hooked;
    private Multimap<String, BiConsumer<Player, Boolean>> listeners = MultimapBuilder.hashKeys().arrayListValues().build();

    public LibreLoginAuthentication() {
        super(Authentication.class);
    }

    @Override
    public void register() {
        super.register();
        libreLogin = ((LibreLoginProvider<Player, World>) hooked).getLibreLogin();
        libreLogin.getEventProvider().subscribe(AuthenticatedEvent.class, event -> {
            for (BiConsumer<Player, Boolean> listener : listeners.values()) {
                listener.accept((Player) event.getPlayer(), false);
            }
        });
    }

    @Override
    public boolean isEnabled() {
        return getHooked() != null ? hooked.isEnabled() : Bukkit.getServer().getPluginManager().isPluginEnabled(getName());
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
        return "LibreLogin";
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

    @Override
    public CompletableFuture<Boolean> isRegistered(OfflinePlayer player) {
        if (player.getName() != null) {
            return isRegistered(player.getName());
        }
        return future(() -> false);
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(String playerName) {
        Validate.notNull(playerName, "Player name cannot be null!");
        return asyncFuture(() -> {
            User user = libreLogin.getDatabaseProvider().getByName(playerName);
            return user != null && user.isRegistered();
        });
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID playerId) {
        return asyncFuture(() -> {
            User user = libreLogin.getDatabaseProvider().getByUUID(playerId);
            return user != null && user.isRegistered();
        });
    }

    @Override
    public CompletableFuture<Boolean> isAuthenticated(Player player) {
        return future(() -> libreLogin.getAuthorizationProvider().isAuthorized(player));
    }
}

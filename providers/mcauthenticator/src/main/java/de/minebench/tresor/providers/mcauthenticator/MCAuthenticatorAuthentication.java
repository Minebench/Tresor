package de.minebench.tresor.providers.mcauthenticator;

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

import de.minebench.tresor.Provider;
import de.minebench.tresor.services.authentication.Authentication;
import io.ibj.mcauthenticator.MCAuthenticator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static de.minebench.tresor.TresorUtils.asyncFuture;
import static de.minebench.tresor.TresorUtils.future;

public class MCAuthenticatorAuthentication extends Provider<Authentication, MCAuthenticator> implements Authentication, Listener {

    private MCAuthenticator hooked;

    public MCAuthenticatorAuthentication() {
        super(Authentication.class);
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
    public MCAuthenticator getHooked() {
        if (hooked == null) {
            hooked = (MCAuthenticator) Bukkit.getServer().getPluginManager().getPlugin(getName());
        }
        return hooked;
    }

    @Override
    public String getName() {
        return "MCAuthenticator";
    }

    @Override
    public boolean supports(Feature feature) {
        switch (feature) {
            case ASYNC:
                return true;
        }
        return false;
    }

    @Override
    public void registerAuthListener(Plugin plugin, BiConsumer<Player, Boolean> onAuth) {
        throw new UnsupportedOperationException(getName() + " does not support events!");
    }

    @Override
    public int removeAuthListeners(Plugin plugin) {
        return 0;
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(OfflinePlayer player) {
        return isRegistered(player.getUniqueId());
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(String playerName) {
        return asyncFuture(() -> {
            OfflinePlayer player = hooked.getServer().getOfflinePlayer(playerName);
            return hooked.getCache().get(player.getUniqueId()).is2fa();
        });
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID playerId) {
        return asyncFuture(() -> hooked.getCache().get(playerId).is2fa());
    }

    @Override
    public CompletableFuture<Boolean> isAuthenticated(Player player) {
        return future(() -> hooked.getCache().get(player.getUniqueId()).authenticated());
    }
}

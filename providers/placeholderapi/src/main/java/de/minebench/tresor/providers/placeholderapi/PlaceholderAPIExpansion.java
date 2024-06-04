package de.minebench.tresor.providers.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final PlaceholderAPIPlaceholders provider;

    public PlaceholderAPIExpansion(final PlaceholderAPIPlaceholders provider) {
        this.provider = provider;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "tresor";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Minebench";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(final OfflinePlayer player, @NotNull final String params) {
        return provider.request(player, params);
    }
}

package de.minebench.tresor.providers.placeholderapi;

import de.minebench.tresor.Provider;
import de.minebench.tresor.services.placeholder.Placeholder;
import de.minebench.tresor.services.placeholder.Placeholders;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderAPIPlaceholders extends Provider<Placeholders, PlaceholderAPIPlugin> implements Placeholders {

    private PlaceholderAPIPlugin hooked;

    private final Map<String, Placeholder> placeholders = new HashMap<>();

    public PlaceholderAPIPlaceholders() {
        super(Placeholders.class);

        new PlaceholderAPIExpansion(this).register();
    }

    @Override
    public PlaceholderAPIPlugin getHooked() {
        if (hooked == null)
            hooked = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin(getName());

        return hooked;
    }

    @Override
    public boolean isEnabled() {
        return getHooked() != null && getHooked().isEnabled();
    }

    @Override
    public String getName() {
        return "PlaceholderAPI";
    }

    @Override
    public Map<String, Placeholder> getRegisteredPlaceholders() {
        return this.placeholders;
    }

    @Override
    public Placeholder getPlaceholder(final String key) {
        return this.placeholders.get(key);
    }

    @Override
    public void add(final Placeholder placeholder) {
        this.placeholders.put(placeholder.getKey(), placeholder);
    }

    @Override
    public String replace(final Player player, final String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public String request(final OfflinePlayer player, final String params) {
        final Placeholder placeholder = getPlaceholder(params);

        if (placeholder == null)
            return "N/A";

        return placeholder.request(player);
    }
}

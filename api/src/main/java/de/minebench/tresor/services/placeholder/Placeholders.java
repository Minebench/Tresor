package de.minebench.tresor.services.placeholder;

import de.minebench.tresor.services.TresorServiceProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Placeholders extends TresorServiceProvider {

    /**
     * Get all registered placeholders
     * @return The placeholders
     */
    Map<String, Placeholder> getRegisteredPlaceholders();

    /**
     * Get a placeholder by its key
     * @param key The key of the placeholder
     * @return The placeholder
     */
    Placeholder getPlaceholder(final String key);

    /**
     * Register a placeholder
     * @param placeholder The placeholder to register
     */
    void add(final Placeholder placeholder);

    /**
     * Replace placeholders in a string
     * @param player The player to replace the placeholders for
     * @param text The text to replace the placeholders in
     * @return The text with replaced placeholders
     */
    String replace(final Player player, final String text);

    default List<String> replace(final Player player, final List<String> text){
        return text
                .stream()
                .map(line -> replace(player, line))
                .collect(Collectors.toList());
    }

    /**
     * Register placeholders into placeholder plugin
     */
    void hook();

    /**
     * Request the value of the placeholder
     * @param player The player to request the value for
     * @param params The parameters for the placeholder
     * @return The value
     */
    String request(final OfflinePlayer player, final String params);

}

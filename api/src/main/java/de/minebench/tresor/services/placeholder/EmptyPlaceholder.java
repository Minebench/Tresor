package de.minebench.tresor.services.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EmptyPlaceholder implements Placeholder {

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public String request(OfflinePlayer player) {
        return "N/A";
    }
}

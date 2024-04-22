package de.minebench.tresor.services.npc;

import java.util.UUID;
import org.bukkit.Location;

public interface NPC {

    UUID getUniqueId();

    boolean isPersistent();

    NPCMetadata getMetadata();
    void updateMetadata(NPCMetadata metadata);

    Location getLocation();
    void teleport(Location location);

    void destroy();
}

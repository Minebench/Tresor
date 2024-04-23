package de.minebench.tresor.services.npc;

import de.minebench.tresor.services.npc.data.Interaction;
import de.minebench.tresor.services.npc.data.NPCMetadata;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Location;

public interface NPC {

    UUID getUniqueId();

    boolean isPersistent();

    NPCMetadata getMetadata();
    void updateMetadata(NPCMetadata metadata);

    Location getLocation();
    void teleport(Location location);

    void onInteract(Consumer<Interaction> action);

    void remove();
}

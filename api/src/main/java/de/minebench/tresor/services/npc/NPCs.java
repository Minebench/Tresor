package de.minebench.tresor.services.npc;

import de.minebench.tresor.services.TresorServiceProvider;
import java.util.UUID;
import org.bukkit.Location;

public interface NPCs extends TresorServiceProvider {

    default NPC createNPC(String name, Location location) {
        return createNPC(name, location, true);
    }

    NPC createNPC(String name, Location location, boolean persistent);
    NPC getNPC(UUID uniqueId);

    void removeNPC(UUID uniqueId);

    boolean supports(Feature feature);

    enum Feature {
        PERSISTENT
    }

}

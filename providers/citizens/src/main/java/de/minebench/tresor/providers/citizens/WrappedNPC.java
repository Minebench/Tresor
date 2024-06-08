package de.minebench.tresor.providers.citizens;

import de.minebench.tresor.services.npc.NPC;
import de.minebench.tresor.services.npc.data.Interaction;
import de.minebench.tresor.services.npc.data.NPCMetadata;
import de.minebench.tresor.services.npc.data.NPCSkinData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class WrappedNPC implements NPC {

    private final List<Consumer<Interaction>> interactActions = new ArrayList<>();
    private final net.citizensnpcs.api.npc.NPC npc;
    private final boolean persistent;

    public WrappedNPC(net.citizensnpcs.api.npc.NPC npc, boolean persistent) {
        this.npc = npc;
        this.persistent = persistent;
    }

    @Override
    public UUID getUniqueId() {
        return npc.getUniqueId();
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public NPCMetadata getMetadata() {
        String displayName = npc.getName();
        SkinTrait skinTrait = npc.getTraitNullable(SkinTrait.class);

        if (skinTrait == null) {
            return new NPCMetadata(displayName, null);
        }

        NPCSkinData skinData = new NPCSkinData(skinTrait.getTexture(), skinTrait.getSignature());
        return new NPCMetadata(displayName, skinData);
    }

    @Override
    public void updateMetadata(NPCMetadata metadata) {
        npc.setName(metadata.getDisplayName());

        NPCSkinData skinData = metadata.getSkinData();

        if (skinData == null) {
            return;
        }

        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setTexture(skinData.getTexture(), skinData.getSignature());
    }

    @Override
    public Location getLocation() {
        return npc.getStoredLocation();
    }

    @Override
    public void teleport(Location location) {
        npc.teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public void onInteract(Consumer<Interaction> action) {
        interactActions.add(action);
    }

    @Override
    public void remove() {
        npc.destroy();
    }

    public void handleInteraction(Interaction interaction) { // I don't think this should be exposed
        for (Consumer<Interaction> action : interactActions) {
            action.accept(interaction);
        }
    }
}

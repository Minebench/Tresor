package de.minebench.tresor.providers.citizens;

import de.minebench.tresor.Provider;
import de.minebench.tresor.services.npc.NPC;
import de.minebench.tresor.services.npc.NPCs;
import java.util.UUID;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class CitizensNPCs extends Provider<NPCs, CitizensPlugin> implements NPCs {

    private NPCRegistry persistentRegistry;
    private NPCRegistry temporaryRegistry;

    private CitizensPlugin hooked;

    public CitizensNPCs() {
        super(NPCs.class);
    }

    @Override
    public boolean isEnabled() {
        return getHooked() != null && getHooked().isEnabled();
    }

    @Override
    public NPC createNPC(String name, Location location, boolean persistent) {
        NPCRegistry registry = persistent ? persistentRegistry : temporaryRegistry;
        net.citizensnpcs.api.npc.NPC rawNPC = registry.createNPC(EntityType.PLAYER, name);
        rawNPC.spawn(location);

        return new WrappedNPC(rawNPC, persistent);
    }

    @Override
    public NPC getNPC(UUID uniqueId) {
        net.citizensnpcs.api.npc.NPC rawNPC = persistentRegistry.getByUniqueIdGlobal(uniqueId);
        boolean isPersistent = true;

        if (rawNPC == null) {
            rawNPC = temporaryRegistry.getByUniqueIdGlobal(uniqueId);
            isPersistent = false;
        }

        if (rawNPC == null) {
            return null;
        }

        return new WrappedNPC(rawNPC, isPersistent);
    }

    @Override
    public void removeNPC(UUID uniqueId) {
        NPC npc = getNPC(uniqueId);

        if (npc != null) {
            npc.destroy();
        }
    }

    @Override
    public boolean supports(Feature feature) {
        return feature == Feature.PERSISTENT;
    }

    @Override
    public CitizensPlugin getHooked() {
        if(hooked == null) {
            hooked = (CitizensPlugin) Bukkit.getPluginManager().getPlugin("Citizens");
            ensureRegistries();
        }

        return hooked;
    }

    @Override
    public String getName() {
        return "Citizens";
    }

    private void ensureRegistries() {
        if (persistentRegistry == null) {
            persistentRegistry = hooked.getNPCRegistry();
        }

        if (temporaryRegistry == null) {
            temporaryRegistry = hooked.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        }
    }
}

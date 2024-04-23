package de.minebench.tresor.providers.citizens.listener;

import de.minebench.tresor.providers.citizens.CitizensNPCs;
import de.minebench.tresor.providers.citizens.WrappedNPC;
import de.minebench.tresor.services.npc.data.Interaction;
import de.minebench.tresor.services.npc.data.Interaction.Type;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CitizensNPCListener implements Listener {

    private final CitizensNPCs citizensNPCs;

    public CitizensNPCListener(CitizensNPCs citizensNPCs) {
        this.citizensNPCs = citizensNPCs;
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        handleInteraction(event.getClicker(), event.getNPC(), Type.LEFT_CLICK);
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        handleInteraction(event.getClicker(), event.getNPC(), Type.RIGHT_CLICK);
    }

    private void handleInteraction(Player player, NPC citizensNPC, Type interactionType) {
        ItemStack item = player.getInventory().getItemInMainHand();
        de.minebench.tresor.services.npc.NPC wrappedNPC = citizensNPCs.getNPC(citizensNPC.getUniqueId());

        if(!(wrappedNPC instanceof WrappedNPC)) { // Doubles as a null check
            return;
        }

        WrappedNPC wrappedCitizensNPC = (WrappedNPC) wrappedNPC;
        Interaction interaction = new Interaction(player, interactionType, item);
        wrappedCitizensNPC.handleInteraction(interaction);

    }




}

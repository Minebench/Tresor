package de.minebench.tresor.services.npc.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Interaction {

    private final Player player;
    private final Type type;
    private final ItemStack heldItem;

    public Interaction(Player player, Type type, ItemStack heldItem) {
        this.player = player;
        this.type = type;
        this.heldItem = heldItem;
    }

    public Player getPlayer() {
        return player;
    }

    public Type getType() {
        return type;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public enum Type {
        LEFT_CLICK,
        RIGHT_CLICK
    }
}

/* This file is part of Vault.

    Vault is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Vault is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Vault.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.milkbowl.vault.chat;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The main Chat API - allows for Prefix/Suffix nodes along with generic Info nodes if the linked Chat system supports them
 */
public abstract class Chat {
    
    private Permission perms;
    
    public Chat(Permission perms) {
        this.perms = perms;
    }
    
    /**
     * Get the world name or null if world is null
     *
     * @param world The world
     * @return      The name of the world or null
     */
    private static String getWorldName(World world) {
        return world != null ? world.getName() : null;
    }
    
    /**
     * Gets name of permission method
     *
     * @return Name of Permission Method
     */
    abstract public String getName();
    
    /**
     * Checks if permission method is enabled.
     *
     * @return Success or Failure
     */
    abstract public boolean isEnabled();
    
    /**
     * Get players prefix
     * Use NULL for world if requesting a global prefix
     *
     * @param world  World name
     * @param player Player name
     * @return Prefix
     */
    abstract public String getPlayerPrefix(String world, String player);
    
    /**
     * Get players prefix
     * Use NULL for world if requesting a global prefix
     *
     * @param world    World name
     * @param playerId Player UUID
     * @return Prefix
     */
    abstract public String getPlayerPrefix(String world, UUID playerId);
    
    /**
     * Get a players prefix in the given world
     * Use NULL for world if requesting a global prefix
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @return Prefix
     */
    public String getPlayerPrefix(String world, OfflinePlayer player) {
        return getPlayerPrefix(world, player.getName());
    }
    
    /**
     * Get players prefix from the world they are currently in.
     * May or may not return the global prefix depending on implementation.
     *
     * @param player Player Object
     * @return Prefix
     */
    public String getPlayerPrefix(Player player) {
        return getPlayerPrefix(player.getWorld().getName(), player);
    }
    
    /**
     * Set players prefix
     * Use NULL for world if setting a global prefix
     *
     * @param world  World name
     * @param player Player name
     * @param prefix Prefix
     */
    abstract public void setPlayerPrefix(String world, String player, String prefix);
    
    /**
     * Set players prefix
     * Use NULL for world if setting a global prefix
     *
     * @param world    World name
     * @param playerId Player UUID
     * @param prefix   Prefix
     */
    abstract public void setPlayerPrefix(String world, UUID playerId, String prefix);
    
    /**
     * Sets players prefix in the given world.
     * Use NULL for world if setting a global prefix
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @param prefix Prefix
     */
    public void setPlayerPrefix(String world, OfflinePlayer player, String prefix) {
        setPlayerPrefix(world, player.getName(), prefix);
    }
    
    /**
     * Set players prefix
     * May or may not set the global prefix depending on implementation.
     *
     * @param player Player Object
     * @param prefix Prefix
     */
    public void setPlayerPrefix(Player player, String prefix) {
        setPlayerPrefix(player.getWorld().getName(), player, prefix);
    }
    
    /**
     * Get players suffix
     * Use NULL for world if requesting a global suffix
     *
     * @param world  World name
     * @param player Player name
     * @return Suffix
     */
    abstract public String getPlayerSuffix(String world, String player);
    
    /**
     * Get players suffix
     * Use NULL for world if requesting a global suffix
     *
     * @param world    World name
     * @param playerId Player UUID
     * @return Suffix
     */
    abstract public String getPlayerSuffix(String world, UUID playerId);
    
    /**
     * Get players suffix in the specified world.
     * Use NULL for world if requesting a global suffix
     *
     * @param world  World name
     * @param player OfflinePlayer name
     * @return Suffix
     */
    public String getPlayerSuffix(String world, OfflinePlayer player) {
        return getPlayerSuffix(world, player.getName());
    }
    
    /**
     * Get players suffix in the world they are currently in.
     * May or may not return the global suffix depending on implementation.
     *
     * @param player Player Object
     * @return Suffix
     */
    public String getPlayerSuffix(Player player) {
        return getPlayerSuffix(player.getWorld().getName(), player);
    }
    
    /**
     * Set players suffix
     * Use NULL for world if setting a global suffix
     *
     * @param world  World name
     * @param player Player name
     * @param suffix Suffix
     */
    abstract public void setPlayerSuffix(String world, String player, String suffix);
    
    /**
     * Set players suffix
     * Use NULL for world if setting a global suffix
     *
     * @param world    World name
     * @param playerId Player UUID
     * @param suffix   Suffix
     */
    abstract public void setPlayerSuffix(String world, UUID playerId, String suffix);
    
    /**
     * Set players suffix for the world specified
     * Use NULL for world if setting a global suffix
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @param suffix Suffix
     */
    public void setPlayerSuffix(String world, OfflinePlayer player, String suffix) {
        setPlayerSuffix(world, player.getName(), suffix);
    }
    
    /**
     * Set players suffix in the world they currently occupy.
     *
     * @param player Player Object
     * @param suffix Suffix
     */
    public void setPlayerSuffix(Player player, String suffix) {
        setPlayerSuffix(player.getWorld().getName(), player, suffix);
    }
    
    /**
     * Get group prefix
     * Use NULL for world if request a global prefix
     *
     * @param world World name
     * @param group Group name
     * @return Prefix
     */
    abstract public String getGroupPrefix(String world, String group);
    
    /**
     * Get group prefix
     * Use NULL for world if request a global prefix
     * @deprecated Since Tresor 1.0; use {{@link #getGroupPrefix(String, String)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @return Prefix
     */
    @Deprecated
    public String getGroupPrefix(World world, String group) {
        return getGroupPrefix(getWorldName(world), group);
    }
    
    /**
     * Set group prefix
     * Use NULL for world if setting a global prefix
     *
     * @param world  World name
     * @param group  Group name
     * @param prefix Prefix
     */
    abstract public void setGroupPrefix(String world, String group, String prefix);
    
    /**
     * Set group prefix
     * Use NULL for world if setting a global prefix
     * @deprecated Since Tresor 1.0; use {{@link #setGroupPrefix(String, String, String)} instead.
     *
     * @param world  World Object
     * @param group  Group name
     * @param prefix Prefix
     */
    @Deprecated
    public void setGroupPrefix(World world, String group, String prefix) {
        setGroupPrefix(getWorldName(world), group, prefix);
    }
    
    /**
     * Get group suffix
     * Use NULL for world if requesting a global suffix
     *
     * @param world World name
     * @param group Group name
     * @return Suffix
     */
    abstract public String getGroupSuffix(String world, String group);
    
    /**
     * Get group suffix
     * Use NULL for world if requesting a global suffix
     * @deprecated Since Tresor 1.0; use {{@link #getGroupSuffix(String, String)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @return Suffix
     */
    @Deprecated
    public String getGroupSuffix(World world, String group) {
        return getGroupSuffix(getWorldName(world), group);
    }
    
    /**
     * Set group suffix
     * Use NULL for world if setting a global suffix
     *
     * @param world  World name
     * @param group  Group name
     * @param suffix Suffix
     */
    abstract public void setGroupSuffix(String world, String group, String suffix);
    
    /**
     * Set group suffix
     * Use NULL for world if setting a global suffix
     * @deprecated Since Tresor 1.0; use {{@link #setGroupSuffix(String, String, String)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @param suffix Suffix
     */
    @Deprecated
    public void setGroupSuffix(World world, String group, String suffix) {
        setGroupSuffix(getWorldName(world), group, suffix);
    }
    
    /**
     * Get a players informational node (Integer) value
     * Use NULL for world for requesting global info
     *
     * @param world        World name
     * @param player       OfflinePlayer
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public int getPlayerInfoInteger(String world, OfflinePlayer player, String node, int defaultValue) {
        return getPlayerInfoInteger(world, player.getName(), node, defaultValue);
    }
    
    /**
     * Get a players informational node (Integer) value
     * Use NULL for world for requesting global info
     *
     * @param world        World name
     * @param player       Player name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public int getPlayerInfoInteger(String world, String player, String node, int defaultValue);
    
    /**
     * Get a players informational node (Integer) value
     * Use NULL for world for requesting global info
     *
     * @param world        World name
     * @param playerId     Player UUID
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public int getPlayerInfoInteger(String world, UUID playerId, String node, int defaultValue);
    
    
    /**
     * Get a players informational node (Integer) value
     * May or may not return the global info depending on implementation.
     *
     * @param player       Player Object
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public int getPlayerInfoInteger(Player player, String node, int defaultValue) {
        return getPlayerInfoInteger(player.getWorld().getName(), player, node, defaultValue);
    }
    
    /**
     * Set a players informational node (Integer) value
     * Use NULL for world for setting global info
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoInteger(String world, OfflinePlayer player, String node, int value) {
        setPlayerInfoInteger(world, player.getName(), node, value);
    }
    
    /**
     * Set a players informational node (Integer) value
     * Use NULL for world for setting global info
     *
     * @param world  World name
     * @param player Player name
     * @param node   Permission node
     * @param value  Value to set
     */
    abstract public void setPlayerInfoInteger(String world, String player, String node, int value);
    
    /**
     * Set a players informational node (Integer) value
     *
     * @param world    World name
     * @param playerId Player UUID
     * @param node     Permission node
     * @param value    Value to set
     */
    abstract public void setPlayerInfoInteger(String world, UUID playerId, String node, int value);
    
    /**
     * Set a players informational node (Integer) value
     *
     * @param player Player Object
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoInteger(Player player, String node, int value) {
        setPlayerInfoInteger(player.getWorld().getName(), player, node, value);
    }
    
    /**
     * Get a groups informational node (Integer) value
     *
     * @param world        World name
     * @param group        Group name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public int getGroupInfoInteger(String world, String group, String node, int defaultValue);
    
    /**
     * Get a groups informational node (Integer) value
     * @deprecated Since Tresor 1.0; use {{@link #getGroupInfoInteger(String, String, String, int)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @param node Permission node
     * @param defaultValue Default value
     * @return Value
     */
    @Deprecated
    public int getGroupInfoInteger(World world, String group, String node, int defaultValue) {
        return getGroupInfoInteger(getWorldName(world), group, node, defaultValue);
    }
    
    /**
     * Set a groups informational node (Integer) value
     *
     * @param world World name
     * @param group Group name
     * @param node Permission node
     * @param value Value to set
     */
    abstract public void setGroupInfoInteger(String world, String group, String node, int value);
    
    /**
     * Set a groups informational node (Integer) value
     * @deprecated Since Tresor 1.0; use {{@link #setGroupInfoInteger(String, String, String, int)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @param node Permission node
     * @param value Value to set
     */
    @Deprecated
    public void setGroupInfoInteger(World world, String group, String node, int value) {
        setGroupInfoInteger(getWorldName(world), group, node, value);
    }
    
    /**
     * Get a players informational node (Double) value
     *
     * @param world        World name
     * @param player       OfflinePlayer
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public double getPlayerInfoDouble(String world, OfflinePlayer player, String node, double defaultValue) {
        return getPlayerInfoDouble(world, player.getName(), node, defaultValue);
    }
    
    /**
     * Get a players informational node (Double) value
     *
     * @param world        World name
     * @param player       Player name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public double getPlayerInfoDouble(String world, String player, String node, double defaultValue);
    
    /**
     * Get a players informational node (Double) value
     *
     * @param world        World name
     * @param playerId     Player UUID
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public double getPlayerInfoDouble(String world, UUID playerId, String node, double defaultValue);
    
    /**
     * Get a players informational node (Double) value
     *
     * @param player       Player Object
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public double getPlayerInfoDouble(Player player, String node, double defaultValue) {
        return getPlayerInfoDouble(player.getWorld().getName(), player, node, defaultValue);
    }
    
    /**
     * Set a players informational node (Double) value
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoDouble(String world, OfflinePlayer player, String node, double value) {
        setPlayerInfoDouble(world, player.getName(), node, value);
    }
    
    /**
     * Set a players informational node (Double) value
     *
     * @param world  World name
     * @param player Player name
     * @param node   Permission node
     * @param value  Value to set
     */
    abstract public void setPlayerInfoDouble(String world, String player, String node, double value);
    
    /**
     * Set a players informational node (Double) value
     *
     * @param world     World name
     * @param playerId  Player UUID
     * @param node      Permission node
     * @param value     Value to set
     */
    abstract public void setPlayerInfoDouble(String world, UUID playerId, String node, double value);
    
    /**
     * Set a players informational node (Double) value
     *
     * @param player Player Object
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoDouble(Player player, String node, double value) {
        setPlayerInfoDouble(player.getWorld().getName(), player, node, value);
    }
    
    /**
     * Get a groups informational node (Double) value
     *
     * @param world        World name
     * @param group        Group name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public double getGroupInfoDouble(String world, String group, String node, double defaultValue);
    
    /**
     * Get a groups informational node (Double) value
     * @deprecated Since Tresor 1.0; use {{@link #getGroupInfoDouble(String, String, String, double)} instead.
     *
     * @param world        World Object
     * @param group        Group name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    @Deprecated
    public double getGroupInfoDouble(World world, String group, String node, double defaultValue) {
        return getGroupInfoDouble(world.getName(), group, node, defaultValue);
    }
    
    /**
     * Set a groups informational node (Double) value
     *
     * @param world World name
     * @param group Group name
     * @param node  Permission node
     * @param value Value to set
     */
    abstract public void setGroupInfoDouble(String world, String group, String node, double value);
    
    /**
     * Set a groups informational node (Double) value
     * @deprecated Since Tresor 1.0; use {{@link #setGroupInfoDouble(String, String, String, double)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @param node  Permission node
     * @param value Value to set
     */
    @Deprecated
    public void setGroupInfoDouble(World world, String group, String node, double value) {
        setGroupInfoDouble(world.getName(), group, node, value);
    }
    
    /**
     * Get a players informational node (Boolean) value
     *
     * @param world        World name
     * @param player       OfflinePlayer
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public boolean getPlayerInfoBoolean(String world, OfflinePlayer player, String node, boolean defaultValue) {
        return getPlayerInfoBoolean(world, player.getName(), node, defaultValue);
    }
    
    /**
     * Get a players informational node (Boolean) value
     *
     * @param world        World name
     * @param player       Player name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public boolean getPlayerInfoBoolean(String world, String player, String node, boolean defaultValue);
    
    /**
     * Get a players informational node (Boolean) value
     *
     * @param world        World name
     * @param playerId       Player uuid
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public boolean getPlayerInfoBoolean(String world, UUID playerId, String node, boolean defaultValue);
    
    /**
     * Get a players informational node (Boolean) value
     *
     * @param player       Player Object
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public boolean getPlayerInfoBoolean(Player player, String node, boolean defaultValue) {
        return getPlayerInfoBoolean(player.getWorld().getName(), player, node, defaultValue);
    }
    
    /**
     * Set a players informational node (Boolean) value
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoBoolean(String world, OfflinePlayer player, String node, boolean value) {
        setPlayerInfoBoolean(world, player.getName(), node, value);
    }
    
    /**
     * Set a players informational node (Boolean) value
     *
     * @param world  World name
     * @param player Player name
     * @param node   Permission node
     * @param value  Value to set
     */
    abstract public void setPlayerInfoBoolean(String world, String player, String node, boolean value);
    
    /**
     * Set a players informational node (Boolean) value
     *
     * @param world     World name
     * @param playerId  Player UUID
     * @param node      Permission node
     * @param value     Value to set
     */
    abstract public void setPlayerInfoBoolean(String world, UUID playerId, String node, boolean value);
    
    /**
     * Set a players informational node (Boolean) value
     *
     * @param player Player Object
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoBoolean(Player player, String node, boolean value) {
        setPlayerInfoBoolean(player.getWorld().getName(), player, node, value);
    }
    
    /**
     * Get a groups informational node (Boolean) value
     *
     * @param world        Name of World
     * @param group        Name of Group
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public boolean getGroupInfoBoolean(String world, String group, String node, boolean defaultValue);
    
    /**
     * Set a players informational node (Boolean) value
     * @deprecated Since Tresor 1.0; use {{@link #getGroupInfoBoolean(String, String, String, boolean)} instead.
     *
     * @param world        World Object
     * @param group        Group name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    @Deprecated
    public boolean getGroupInfoBoolean(World world, String group, String node, boolean defaultValue) {
        return getGroupInfoBoolean(world.getName(), group, node, defaultValue);
    }
    
    /**
     * Set a groups informational node (Boolean) value
     *
     * @param world World name
     * @param group Group name
     * @param node  Permission node
     * @param value Value to set
     */
    abstract public void setGroupInfoBoolean(String world, String group, String node, boolean value);
    
    /**
     * Set a players informational node (Boolean) value
     * @deprecated Since Tresor 1.0; use {{@link #setGroupInfoBoolean(String, String, String, boolean)} instead.
     *
     * @param world World Object
     * @param group Group name
     * @param node  Permission node
     * @param value Value to set
     */
    @Deprecated
    public void setGroupInfoBoolean(World world, String group, String node, boolean value) {
        setGroupInfoBoolean(world.getName(), group, node, value);
    }
    
    /**
     * Get a players informational node (String) value
     *
     * @param world        World name
     * @param player       OfflinePlayer
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public String getPlayerInfoString(String world, OfflinePlayer player, String node, String defaultValue) {
        return getPlayerInfoString(world, player.getName(), node, defaultValue);
    }
    
    /**
     * Get a players informational node (String) value
     *
     * @param world        World name
     * @param player       Player name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public String getPlayerInfoString(String world, String player, String node, String defaultValue);
    
    /**
     * Get a players informational node (String) value
     *
     * @param world        World name
     * @param playerId     Player UUID
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public String getPlayerInfoString(String world, UUID playerId, String node, String defaultValue);
    
    /**
     * Get a players informational node (String) value
     *
     * @param world        World Object
     * @param player       Player name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public String getPlayerInfoString(World world, String player, String node, String defaultValue) {
        return getPlayerInfoString(world.getName(), player, node, defaultValue);
    }
    
    /**
     * Get a players informational node (String) value
     *
     * @param player       Player Object
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    public String getPlayerInfoString(Player player, String node, String defaultValue) {
        return getPlayerInfoString(player.getWorld().getName(), player, node, defaultValue);
    }
    
    /**
     * Set a players informational node (String) value
     *
     * @param world  World name
     * @param player OfflinePlayer
     * @param node   Permission node
     * @param value  Value to set
     */
    public void setPlayerInfoString(String world, OfflinePlayer player, String node, String value) {
        setPlayerInfoString(world, player.getName(), node, value);
    }
    
    /**
     * Set a players informational node (String) value
     *
     * @param world  World name
     * @param player Player name
     * @param node   Permission node
     * @param value  Value to set
     */
    abstract public void setPlayerInfoString(String world, String player, String node, String value);
    
    /**
     * Set a players informational node (String) value
     *
     * @param world    World name
     * @param playerId Player UUID
     * @param node     Permission node
     * @param value    Value to set
     */
    abstract public void setPlayerInfoString(String world, UUID playerId, String node, String value);
    
    /**
     * Set a players informational node (String) value
     *
     * @param player Player Object
     * @param node   Permission node
     * @param value  Value ot set
     */
    public void setPlayerInfoString(Player player, String node, String value) {
        setPlayerInfoString(player.getWorld().getName(), player, node, value);
    }
    
    /**
     * Get a groups informational node (String) value
     *
     * @param world        Name of World
     * @param group        Name of Group
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    abstract public String getGroupInfoString(String world, String group, String node, String defaultValue);
    
    /**
     * Set a players informational node (String) value
     * @deprecated Since Tresor 1.0; use {{@link #getGroupInfoString(String, String, String, String)} instead.
     *
     * @param world        World Object
     * @param group        Group name
     * @param node         Permission node
     * @param defaultValue Default value
     * @return Value
     */
    @Deprecated
    public String getGroupInfoString(World world, String group, String node, String defaultValue) {
        return getGroupInfoString(world.getName(), group, node, defaultValue);
    }
    
    /**
     * Set a groups informational node (String) value
     *
     * @param world World name
     * @param group Group name
     * @param node  Permission node
     * @param value Value to set
     */
    abstract public void setGroupInfoString(String world, String group, String node, String value);
    
    /**
     * Set a groups informational node (String) value
     * @deprecated Since Tresor 1.0; use {{@link #setGroupInfoString(String, String, String, String)} instead.
     *
     * @param world World name
     * @param group Group name
     * @param node  Permission node
     * @param value Value to set
     */
    @Deprecated
    public void setGroupInfoString(World world, String group, String node, String value) {
        setGroupInfoString(world.getName(), group, node, value);
    }
    
    /**
     * @param world  World name
     * @param player OfflinePlayer
     * @param group  Group name
     * @return Success or Failure
     * @deprecated Since Tresor 1.0; use {{@link Permission#playerInGroup(String, OfflinePlayer, String)} instead.
     * Check if player is member of a group.
     */
    @Deprecated
    public boolean playerInGroup(String world, OfflinePlayer player, String group) {
        return perms.playerInGroup(world, player, group);
    }
    
    /**
     * @param world  World name
     * @param player Player name
     * @param group  Group name
     * @return Success or Failure
     * @deprecated Since Tresor 1.0; use {{@link Permission#playerInGroup(String, String, String)} instead.
     * Check if player is member of a group.
     */
    @Deprecated
    public boolean playerInGroup(String world, String player, String group) {
        return perms.playerInGroup(world, player, group);
    }
    
    /**
     * @param world  World Object
     * @param player Player name
     * @param group  Group name
     * @return Success or Failure
     * @deprecated Since Tresor 1.0; use {{@link Permission#playerInGroup(String, String, String)} instead.
     * Check if player is member of a group.
     */
    @Deprecated
    public boolean playerInGroup(World world, String player, String group) {
        return perms.playerInGroup(world.getName(), player, group);
    }
    
    /**
     * @param player Player Object
     * @param group  Group name
     * @return Success or Failure
     * @deprecated Since Tresor 1.0; use {{@link Permission#playerInGroup(Player, String)} instead.
     * Check if player is member of a group.
     */
    @Deprecated
    public boolean playerInGroup(Player player, String group) {
        return perms.playerInGroup(player, group);
    }
    
    /**
     * @param world  World name
     * @param player OfflinePlayer
     * @return Array of groups
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPlayerGroups(String, OfflinePlayer)} instead.
     * Gets the list of groups that this player has
     */
    @Deprecated
    public String[] getPlayerGroups(String world, OfflinePlayer player) {
        return perms.getPlayerGroups(world, player);
    }
    
    /**
     * @param world  World name
     * @param player Player name
     * @return Array of groups
     * @deprecated As of VaultAPI 1.4 use {{@link #getPlayerGroups(String, OfflinePlayer)} instead.
     * Gets the list of groups that this player has
     */
    @Deprecated
    public String[] getPlayerGroups(String world, String player) {
        return perms.getPlayerGroups(world, player);
    }
    
    /**
     * @param world  World Object
     * @param player Player name
     * @return Array of groups
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPlayerGroups(String, String)} instead.
     * Gets the list of groups that this player has
     */
    @Deprecated
    public String[] getPlayerGroups(World world, String player) {
        return perms.getPlayerGroups(world.getName(), player);
    }
    
    /**
     * @param player Player Object
     * @return Array of groups
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPlayerGroups(Player)} instead.
     * Gets the list of groups that this player has
     */
    @Deprecated
    public String[] getPlayerGroups(Player player) {
        return perms.getPlayerGroups(player.getWorld().getName(), player);
    }
    
    /**
     * @param world  World name
     * @param player OfflinePlayer
     * @return Players primary group
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPrimaryGroup(String, OfflinePlayer)} instead.
     * Gets players primary group
     */
    @Deprecated
    public String getPrimaryGroup(String world, OfflinePlayer player) {
        return perms.getPrimaryGroup(world, player);
    }
    
    /**
     * @param world  World name
     * @param player Player name
     * @return Players primary group
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPrimaryGroup(String, OfflinePlayer)} instead.
     * Gets players primary group
     */
    @Deprecated
    public String getPrimaryGroup(String world, String player) {
        return perms.getPrimaryGroup(world, player);
    }
    
    /**
     * @param world  World Object
     * @param player Player name
     * @return Players primary group
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPrimaryGroup(String, String)} instead.
     * Gets players primary group
     */
    @Deprecated
    public String getPrimaryGroup(World world, String player) {
        return perms.getPrimaryGroup(world.getName(), player);
    }
    
    /**
     * @param player Player Object
     * @return Players primary group
     * @deprecated Since Tresor 1.0; use {{@link Permission#getPrimaryGroup(Player)} instead.
     * Get players primary group
     */
    @Deprecated
    public String getPrimaryGroup(Player player) {
        return perms.getPrimaryGroup(player);
    }
    
    /**
     * @return an Array of String of all groups
     * @deprecated Since Tresor 1.0; use {{@link Permission#getGroups()} instead.
     * Returns a list of all known groups
     */
    @Deprecated
    public String[] getGroups() {
        return perms.getGroups();
    }
}

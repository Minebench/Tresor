package de.minebench.tresor.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2020 Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * The main economy API using industry standard BigDecimal for values
 */
public interface TresorEconomy {

    /**
     * Checks if economy method is enabled.
     *
     * @return Success or Failure
     */
    boolean isEnabled();

    /**
     * Gets name of economy method
     *
     * @return Name of Economy Method
     */
    String getName();

    /**
     * Returns true if the given implementation supports a given feature.
     *
     * @param feature The feature to check for
     * @return true if the implementation supports the feature
     */
    boolean supports(Feature feature);

    /**
     * Some economy plugins round off after a certain number of digits.
     * This function returns the number of digits the plugin keeps
     * or -1 if no rounding occurs.
     *
     * @return number of digits after the decimal point kept
     */
    int fractionalDigits();

    /**
     * Format amount into a human readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount to format
     * @return Human readable string describing amount
     */
    String format(BigDecimal amount);

    /**
     * Returns the name of the currency in plural form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (plural)
     */
    String currencyNamePlural();


    /**
     * Returns the name of the currency in singular form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (singular)
     */
    String currencyNameSingular();

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param playerName to check
     * @return if the player has an account
     */
    boolean hasAccount(String playerName);

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param playerId  to check
     * @return if the player has an account
     */
    boolean hasAccount(UUID playerId);

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player to check
     * @return if the player has an account
     */
    default boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getUniqueId());
    }

    /**
     * Checks if this player has an account on the server yet on the given world
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param playerName to check in the world
     * @param worldName  world-specific account
     * @return if the player has an account
     */
    boolean hasAccount(String playerName, String worldName);

    /**
     * Checks if this player has an account on the server yet on the given world
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param playerId  to check in the world
     * @param worldName world-specific account
     * @return if the player has an account
     */
    boolean hasAccount(UUID playerId, String worldName);

    /**
     * Checks if this player has an account on the server yet on the given world
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player    to check in the world
     * @param worldName world-specific account
     * @return if the player has an account
     */
    default boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player.getUniqueId(), worldName);
    }

    /**
     * Gets balance of a player
     *
     * @param playerId  of the player
     * @return Amount currently held in players account
     */
    BigDecimal getBalance(UUID playerId);

    /**
     * Gets balance of a player
     *
     * @param playerName    of the player
     * @return Amount currently held in players account
     */
    BigDecimal getBalance(String playerName);

    /**
     * Gets balance of a player
     *
     * @param player    to check
     * @return Amount currently held in players account
     */
    default BigDecimal getBalance(OfflinePlayer player) {
        return getBalance(player.getUniqueId());
    }

    /**
     * Gets balance of a player on the specified world.
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerName of the player
     * @param world  name of the world
     * @return Amount currently held in players account
     */
    BigDecimal getBalance(String playerName, String world);

    /**
     * Gets balance of a player on the specified world.
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerId of the player
     * @param world  name of the world
     * @return Amount currently held in players account
     */
    BigDecimal getBalance(UUID playerId, String world);

    /**
     * Gets balance of a player on the specified world.
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player to check
     * @param world  name of the world
     * @return Amount currently held in players account
     */
    default BigDecimal getBalance(OfflinePlayer player, String world) {
        return getBalance(player.getUniqueId(), world);
    }

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerName    of the player to check
     * @param amount        to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    boolean has(String playerName, BigDecimal amount);

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerId  of the player to check
     * @param amount    to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    default boolean has(UUID playerId, BigDecimal amount) {
        return has(Bukkit.getOfflinePlayer(playerId), amount);
    }

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    default boolean has(OfflinePlayer player, BigDecimal amount) {
        return has(player.getUniqueId(), amount);
    }

    /**
     * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerName    of the player to check
     * @param worldName     to check in
     * @param amount        to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    boolean has(String playerName, String worldName, BigDecimal amount);

    /**
     * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerId  of the player to check
     * @param worldName to check in
     * @param amount    to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    boolean has(UUID playerId, String worldName, BigDecimal amount);

    /**
     * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to check
     * @param worldName to check in
     * @param amount    to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    default boolean has(OfflinePlayer player, String worldName, BigDecimal amount) {
        return has(player.getUniqueId(), worldName, amount);
    }

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerName    to withdraw from
     * @param amount        Amount to withdraw
     * @param reason        The reason for this withdrawal e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse withdrawPlayer(String playerName, BigDecimal amount, String reason);

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerId  to withdraw from
     * @param amount    Amount to withdraw
     * @param reason    The reason for this withdrawal e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse withdrawPlayer(UUID playerId, BigDecimal amount, String reason);

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     * @param reason The reason for this withdrawal e.g. the plugin name
     * @return Detailed response of transaction
     */
    default EconomyResponse withdrawPlayer(OfflinePlayer player, BigDecimal amount, String reason) {
        return withdrawPlayer(player.getUniqueId(), amount, reason);
    }

    /**
     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerName    of the player to withdraw from
     * @param worldName     name of the world
     * @param amount        Amount to withdraw
     * @param reason        The reason for this withdrawal e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse withdrawPlayer(String playerName, String worldName, BigDecimal amount, String reason);

    /**
     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerId  of the player to withdraw from
     * @param worldName name of the world
     * @param amount    Amount to withdraw
     * @param reason    The reason for this withdrawal e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse withdrawPlayer(UUID playerId, String worldName, BigDecimal amount, String reason);

    /**
     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to withdraw from
     * @param worldName name of the world
     * @param amount    Amount to withdraw
     * @param reason    The reason for this withdrawal e.g. the plugin name
     * @return Detailed response of transaction
     */
    default EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, BigDecimal amount, String reason) {
        return withdrawPlayer(player.getUniqueId(), worldName, amount, reason);
    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerName    of the player to deposit to
     * @param amount        Amount to deposit
     * @param reason        The reason for this deposit e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse depositPlayer(String playerName, BigDecimal amount, String reason);

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerId  of the player to deposit to
     * @param amount    Amount to deposit
     * @param reason    The reason for this deposit e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse depositPlayer(UUID playerId, BigDecimal amount, String reason);

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     * @param reason The reason for this deposit e.g. the plugin name
     * @return Detailed response of transaction
     */
    default EconomyResponse depositPlayer(OfflinePlayer player, BigDecimal amount, String reason) {
        return depositPlayer(player.getUniqueId(), amount, reason);
    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerName    of the player to deposit to
     * @param worldName     name of the world
     * @param amount        Amount to deposit
     * @param reason        The reason for this deposit e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse depositPlayer(String playerName, String worldName, BigDecimal amount, String reason);

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerId  of the player to deposit to
     * @param worldName name of the world
     * @param amount    Amount to deposit
     * @param reason    The reason for this deposit e.g. the plugin name
     * @return Detailed response of transaction
     */
    EconomyResponse depositPlayer(UUID playerId, String worldName, BigDecimal amount, String reason);

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to deposit to
     * @param worldName name of the world
     * @param amount    Amount to deposit
     * @param reason    The reason for this deposit e.g. the plugin name
     * @return Detailed response of transaction
     */
    default EconomyResponse depositPlayer(OfflinePlayer player, String worldName, BigDecimal amount, String reason) {
        return depositPlayer(player.getUniqueId(), worldName, amount, reason);
    }

    /**
     * Creates a bank account with the specified name and the player as the owner
     *
     * @param name          of account
     * @param playerName    the account should be linked to
     * @return EconomyResponse Object
     */
    EconomyResponse createBank(String name, String playerName);

    /**
     * Creates a bank account with the specified name and the player as the owner
     *
     * @param name      of account
     * @param playerId  the account should be linked to
     * @return EconomyResponse Object
     */
    EconomyResponse createBank(String name, UUID playerId);

    /**
     * Creates a bank account with the specified name and the player as the owner
     *
     * @param name   of account
     * @param player the account should be linked to
     * @return EconomyResponse Object
     */
    default EconomyResponse createBank(String name, OfflinePlayer player) {
        return createBank(name, player.getUniqueId());
    }

    /**
     * Deletes a bank account with the specified name.
     *
     * @param name of the back to delete
     * @return if the operation completed successfully
     */
    EconomyResponse deleteBank(String name);

    /**
     * Returns the amount the bank has
     *
     * @param name of the account
     * @return EconomyResponse Object
     */
    EconomyResponse bankBalance(String name);

    /**
     * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to check for
     * @return EconomyResponse Object
     */
    EconomyResponse bankHas(String name, BigDecimal amount);

    /**
     * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to withdraw
     * @param reason The reason for this deposit e.g. the plugin name
     * @return EconomyResponse Object
     */
    EconomyResponse bankWithdraw(String name, BigDecimal amount, String reason);

    /**
     * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to deposit
     * @param reason The reason for this deposit e.g. the plugin name
     * @return EconomyResponse Object
     */
    EconomyResponse bankDeposit(String name, BigDecimal amount, String reason);

    /**
     * Check if a player is the owner of a bank account
     *
     * @param name          of the account
     * @param playerName    of the player to check for ownership
     * @return EconomyResponse Object
     */
    EconomyResponse isBankOwner(String name, String playerName);

    /**
     * Check if a player is the owner of a bank account
     *
     * @param name      of the account
     * @param playerId  of the player to check for ownership
     * @return EconomyResponse Object
     */
    EconomyResponse isBankOwner(String name, UUID playerId);

    /**
     * Check if a player is the owner of a bank account
     *
     * @param name   of the account
     * @param player to check for ownership
     * @return EconomyResponse Object
     */
    default EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return isBankOwner(name, player.getUniqueId());
    }

    /**
     * Check if the player is a member of the bank account
     *
     * @param name          of the account
     * @param playerName    of the player to check membership
     * @return EconomyResponse Object
     */
    EconomyResponse isBankMember(String name, String playerName);

    /**
     * Check if the player is a member of the bank account
     *
     * @param name      of the account
     * @param playerId  of the player to check membership
     * @return EconomyResponse Object
     */
    EconomyResponse isBankMember(String name, UUID playerId);

    /**
     * Check if the player is a member of the bank account
     *
     * @param name   of the account
     * @param player to check membership
     * @return EconomyResponse Object
     */
    default EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return isBankMember(name, player.getUniqueId());
    }

    /**
     * Gets the list of banks
     *
     * @return the List of Banks
     */
    List<String> getBanks();

    /**
     * Attempts to create a player account for the given player
     *
     * @param playerName    the name of the player to create the account for
     * @return if the account creation was successful
     */
    boolean createPlayerAccount(String playerName);

    /**
     * Attempts to create a player account for the given player
     *
     * @param playerId  the name of the player to create the account for
     * @return if the account creation was successful
     */
    boolean createPlayerAccount(UUID playerId);

    /**
     * Attempts to create a player account for the given player
     *
     * @param player    the player to create the account for
     * @return if the account creation was successful
     */
    default boolean createPlayerAccount(OfflinePlayer player) {
        return createPlayerAccount(player.getUniqueId());
    }

    /**
     * Attempts to create a player account for the given player on the specified world
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerName    of the player to create the account for
     * @param worldName     String name of the world
     * @return if the account creation was successful
     */
    boolean createPlayerAccount(String playerName, String worldName);

    /**
     * Attempts to create a player account for the given player on the specified world
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param playerId  of the player to create the account for
     * @param worldName String name of the world
     * @return if the account creation was successful
     */
    boolean createPlayerAccount(UUID playerId, String worldName);

    /**
     * Attempts to create a player account for the given player on the specified world
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    OfflinePlayer
     * @param worldName String name of the world
     * @return if the account creation was successful
     */
    default boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player.getUniqueId(), worldName);
    }

    enum Feature {
        BIG_DECIMAL,
        BANK,
        UUID,
        NATIVE_UUID,
        OFFLINE,
        WORLD,
        LOG;
    }
}

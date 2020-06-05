package de.minebench.tresor.economy;

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

import java.math.BigDecimal;

/**
 * Indicates a typical Return for an Economy method.  
 * It includes a {@link ResponseType} indicating whether the plugin currently being used for Economy actually allows
 * the method, or if the operation was a success or failure.
 *
 */
public class EconomyResponse {

    /**
     * Enum for types of Responses indicating the status of a method call.
     */
    public static enum ResponseType {
        SUCCESS(1),
        FAILURE(2),
        NOT_IMPLEMENTED(3);

        private int id;

        ResponseType(int id) {
            this.id = id;
        }

        int getId() {
            return id;
        }

        public net.milkbowl.vault.economy.EconomyResponse.ResponseType toVault() {
            return net.milkbowl.vault.economy.EconomyResponse.ResponseType.valueOf(name());
        }
    }

    /**
     * Amount modified by calling method
     */
    private final BigDecimal amount;

    /**
     * New balance of account
     */
    private final BigDecimal balance;

    /**
     * Success or failure of call. Using Enum of ResponseType to determine valid
     * outcomes
     */
    private final ResponseType type;

    /**
     * Error message if the variable 'type' is ResponseType.FAILURE
     */
    private final String errorMessage;

    /**
     * Constructor for EconomyResponse
     * @param amount Amount modified during operation
     * @param balance New balance of account
     * @param type Success or failure type of the operation
     * @param errorMessage Error message if necessary (commonly null)
     */
    public EconomyResponse(BigDecimal amount, BigDecimal balance, ResponseType type, String errorMessage) {
        this.amount = amount;
        this.balance = balance;
        this.type = type;
        this.errorMessage = errorMessage;
    }

    /**
     * Get the amount modified by calling method
     * @return The changed amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get the new balance of the account
     * @return The balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Get whether or not the call succeeded or failed. Using Enum of ResponseType to determine valid
     * outcomes
     * @return The ResponseType
     */
    public ResponseType getType() {
        return type;
    }

    /**
     * Error message if the variable 'type' is ResponseType.FAILURE
     * @return The error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Checks if an operation was successful
     * @return whether or not this transaction succeeded
     */
    public boolean transactionSuccess() {
        switch (type) {
        case SUCCESS:
            return true;
        default:
            return false;
        }
    }

    /**
     * Convert to Vault EconomyResponse
     * @return The Vault {@link net.milkbowl.vault.economy.EconomyResponse} used by {@link net.milkbowl.vault.economy.Economy}
     */
    public net.milkbowl.vault.economy.EconomyResponse toVault() {
        return new net.milkbowl.vault.economy.EconomyResponse(
                amount.doubleValue(),
                balance.doubleValue(),
                type.toVault(),
                errorMessage
        );
    }
}
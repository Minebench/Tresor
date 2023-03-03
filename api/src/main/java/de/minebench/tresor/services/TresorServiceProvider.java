package de.minebench.tresor.services;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2023 Max Lee aka Phoenix616 (max@themoep.de)
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
 * A class describing a provider for a service
 */
public interface TresorServiceProvider {
    /**
     * Checks if service provider is enabled.
     *
     * @return Success or Failure
     */
    boolean isEnabled();

    /**
     * Gets name of service provider
     *
     * @return Name of service provider
     */
    String getName();
}

package de.minebench.tresor;

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

import de.themoep.hook.core.Hook;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public abstract class Provider<T, P extends Plugin> implements Hook<P> {

    protected final Class<T> providerClass;
    private final ServicePriority servicePriority;

    public Provider(Class<T> providerClass) {
        this(providerClass, ServicePriority.Normal);
    }

    public Provider(Class<T> providerClass, ServicePriority servicePriority) {
        Validate.isTrue(providerClass.isAssignableFrom(getClass()), getClass().getSimpleName() + " does not implement " + providerClass.getSimpleName());
        this.providerClass = providerClass;
        this.servicePriority = servicePriority;
    }

    @Override
    public void register() {
        getHooked().getServer().getServicesManager().register(providerClass, (T) this, getHooked(), servicePriority);
    }

    @Override
    public void unregister() {
        getHooked().getServer().getServicesManager().unregister(providerClass, this);
    }
}

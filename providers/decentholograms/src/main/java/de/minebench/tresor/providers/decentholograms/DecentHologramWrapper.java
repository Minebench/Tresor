package de.minebench.tresor.providers.decentholograms;

/*
 * Tresor - Abstraction library for Bukkit plugins
 * Copyright (C) 2024 Max Lee aka Phoenix616 (max@themoep.de)
 * Copyright (C) 2024 Tresor Contributors (https://github.com/Minebench/Tresor/graphs/contributors)
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

import de.minebench.tresor.services.hologram.Hologram;
import de.minebench.tresor.services.hologram.Holograms;
import de.minebench.tresor.services.hologram.Holograms.Feature;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DecentHologramWrapper implements Hologram {

    private final eu.decentsoftware.holograms.api.holograms.Hologram hologram;
    private final Holograms provider;

    private double lineHeight = 0.25;

    public DecentHologramWrapper(Holograms provider, eu.decentsoftware.holograms.api.holograms.Hologram hologram) {
        this.provider = provider;
        this.hologram = hologram;
    }

    @Override
    public Holograms getProvider() {
        return provider;
    }

    @Override
    public String getHologramId() {
        return hologram.getId();
    }

    @Override
    public void addLine(String line) {
        HologramPage page = hologram.getPage(0);
        page.addLine(createLine(line));
    }

    @Override
    public void setLine(int index, String line) {
        HologramPage page = hologram.getPage(0);
        page.setLine(index, line);
    }

    @Override
    public void removeLine(int index) {
        HologramPage page = hologram.getPage(0);
        page.removeLine(index);
    }

    @Override
    public String getLine(int index) {
        HologramPage page = hologram.getPage(0);
        return page.getLine(index).getText();
    }

    @Override
    public double getLineHeight() {
        return lineHeight;
    }

    @Override
    public void setLineHeight(double height) {
        lineHeight = height;

        // Update existing holograms
        HologramPage page = hologram.getPage(0);

        for (int index = 0; index < page.getLines().size(); index++) {
            HologramLine line = page.getLine(index);
            line.setLocation(getLineLocation(index));
        }
    }

    @Override
    public Location getLocation() {
        return hologram.getLocation();
    }

    @Override
    public void teleport(Location location) {
        hologram.setLocation(location);
    }

    @Override
    public void showTo(Player player) {
        if (!supports(Feature.PER_PLAYER)) {
            throw new UnsupportedOperationException("This hologram does not support per-player visibility");
        }

        hologram.show(player, 0);
    }

    @Override
    public void hideFrom(Player player) {
        if (!supports(Feature.PER_PLAYER)) {
            throw new UnsupportedOperationException("This hologram does not support per-player visibility");
        }

        hologram.hide(player);
    }

    @Override
    public void destroy() {
        hologram.delete();
    }

    private HologramLine createLine(String text) {
        HologramPage page = hologram.getPage(0);
        int index = page.getLines().size();
        return new HologramLine(page, getLineLocation(index), text);
    }

    private Location getLineLocation(int index) {
        return hologram.getLocation().clone().subtract(0, lineHeight * index, 0);
    }
}

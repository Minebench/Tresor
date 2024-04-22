package de.minebench.tresor.providers.decentholograms;

import de.minebench.tresor.services.hologram.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DecentHologramWrapper implements Hologram {

    private final eu.decentsoftware.holograms.api.holograms.Hologram hologram;
    private double lineHeight = 0.25;

    public DecentHologramWrapper(eu.decentsoftware.holograms.api.holograms.Hologram hologram) {
        this.hologram = hologram;
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

    @Override
    public boolean supports(Feature feature) {
        if (feature == Feature.PER_PLAYER) {
            return true;
        }

        return false;
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

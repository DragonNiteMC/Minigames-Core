package com.ericlam.mc.minigames.core.implement;

import com.ericlam.mc.minigames.core.arena.Arena;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public final class ArenaInfo implements Arena {

    private final String author;
    private final String displayName;
    private final String arenaName;
    private final World world;
    private final Map<String, List<Location>> locMap;
    private final List<String> description;

    public ArenaInfo(String author, String displayName, String arenaName, World world, Map<String, List<Location>> locMap, List<String> description) {
        this.author = author;
        this.displayName = displayName;
        this.arenaName = arenaName;
        this.world = world;
        this.locMap = locMap;
        this.description = description;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public String getArenaName() {
        return arenaName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Map<String, List<Location>> getLocationsMap() {
        return locMap;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public String[] getInfo() {
        throw new UnsupportedOperationException("getInfo Method is not allowed in this operation");
    }
}

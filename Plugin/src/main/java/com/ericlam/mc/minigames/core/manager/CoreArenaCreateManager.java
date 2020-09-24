package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.arena.ArenaMechanic;
import com.ericlam.mc.minigames.core.arena.CreateArena;
import com.ericlam.mc.minigames.core.exception.NoMoreElementException;
import com.ericlam.mc.minigames.core.exception.arena.create.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class CoreArenaCreateManager implements ArenaCreateManager {

    private final File arenaFolder;
    private final ImmutableMap<String, Integer> allowedWarps;
    private final ArenaConfig arenaConfig;
    private final Map<String, CreateArena> preCreation = new HashMap<>();
    @Inject
    private ArenaMechanic arenaMechanic;
    @Inject
    private ArenaManager arenaManager;

    @Inject
    public CoreArenaCreateManager(ArenaConfig arenaConfig) {
        this.arenaConfig = arenaConfig;
        this.allowedWarps = arenaConfig.getAllowWarps();
        this.arenaFolder = arenaConfig.getArenaFolder();
    }

    void addAll(List<CreateArena> createArenas) {
        createArenas.forEach(c -> preCreation.put(c.getArenaName(), c));
    }

    public CompletableFuture<Boolean> setLobbyLocation(final Location location) {
        return arenaConfig.setLobbyLocation(location);
    }

    private CreateArena validate(String arena) throws ArenaNotExistException {
        return Optional.ofNullable(preCreation.get(arena)).orElseThrow(() -> new ArenaNotExistException(arena));
    }

    @Override
    public void setAuthor(String arena, String author) throws ArenaNotExistException {
        CreateArena createArena = validate(arena);
        createArena.setAuthor(author);
        createArena.setChanged(true);
    }

    @Override
    public void setDisplayName(String arena, String displayName) throws ArenaNotExistException {
        CreateArena createArena = validate(arena);
        createArena.setDisplayName(displayName);
        createArena.setChanged(true);
    }

    @Override
    public void setName(String arena, String newName) throws ArenaNotExistException, ArenaNameExistException {
        if (preCreation.containsKey(newName)) throw new ArenaNameExistException(newName);
        CreateArena createArena = validate(arena);
        createArena.setArenaName(newName);
        createArena.setChanged(true);
        preCreation.remove(arena);
        preCreation.put(newName, createArena);
        this.deleteArena(arenaFolder, arena).thenComposeAsync((b) -> {
            if (b) return this.saveArena(arenaFolder, createArena);
            else return CompletableFuture.completedFuture(false);
        });
    }

    @Override
    public void addSpawn(String arena, String warp, Location location) throws ArenaNotExistException, WarpNotExistException, LocationMaxReachedException {
        CreateArena createArena = validate(arena);
        int max = Optional.ofNullable(allowedWarps.get(warp)).orElseThrow(() -> new WarpNotExistException(warp));
        createArena.addLocation(warp, location, max);
        createArena.setChanged(true);
    }

    @Override
    public void removeSpawn(String arena, String warp) throws ArenaNotExistException, WarpNotExistException, NoMoreLocationException {
        CreateArena createArena = validate(arena);
        createArena.removeLastLocation(warp);
        createArena.setChanged(true);
    }

    @Override
    public void createWarp(String arena, String warp) throws ArenaNotExistException, WarpExistException, IllegalWarpException {
        CreateArena createArena = validate(arena);
        if (!allowedWarps.containsKey(warp))
            throw new IllegalWarpException(warp, allowedWarps.keySet().toArray(String[]::new));
        createArena.addWarp(warp);
        createArena.setChanged(true);

    }

    @Override
    public void removeWarp(String arena, String warp) throws ArenaNotExistException, WarpNotExistException {
        CreateArena createArena = validate(arena);
        createArena.removeWarp(warp);
        createArena.setChanged(true);
    }

    @Override
    public CompletableFuture<Boolean> saveArena(String arena) throws SetUpNotFinishException, ArenaNotExistException, ArenaUnchangedExcpetion, ArenaNotBackupException {
        CreateArena createArena = validate(arena);
        if (!createArena.isChanged()) throw new ArenaUnchangedExcpetion(arena);
        if (!createArena.isSetupCompleted()) throw new SetUpNotFinishException(arena);
        if (!((CoreArenaManager) arenaManager).isBackedUp(createArena)) throw new ArenaNotBackupException(arena);
        return this.saveArena(arenaFolder, createArena).thenApply(b -> {
            if (b) createArena.setChanged(false);
            return b;
        });
    }

    @Override
    public CompletableFuture<File> backupArena(String arena) throws ArenaNotExistException, BackupNotAllowedException {
        CreateArena createArena = validate(arena);
        return ((CoreArenaManager) arenaManager).backupArena(createArena);
    }

    private CompletableFuture<Boolean> saveArena(File folder, Arena arena) {
        return CompletableFuture.supplyAsync(() -> {
            final String arenaName = arena.getArenaName();
            File yml = new File(folder, arenaName.concat(".yml"));
            FileConfiguration preSave = new YamlConfiguration();
            preSave.set("display-name", arena.getDisplayName());
            preSave.set("author", arena.getAuthor());
            preSave.set("world", arena.getWorld().getName());
            preSave.set("description", arena.getDescription());
            arenaMechanic.saveExtraArenaSetting(preSave, arena);
            ConfigurationSection warp = preSave.createSection("warps");
            for (String s : arena.getLocationsMap().keySet()) {
                Map<Integer, Map<String, Object>> locSection = new LinkedHashMap<>();
                int i = 0;
                for (Location location : arena.getWarp(s)) {
                    locSection.put(i++, location.serialize());
                }
                warp.createSection(s, locSection);
            }
            try {
                preSave.save(yml);
                return true;
            } catch (IOException e) {
                return false;
            }
        });
    }

    @Override
    public String[] getArenaInfo(String arena) throws ArenaNotExistException {
        CreateArena createArena = validate(arena);
        return createArena.getInfo();
    }

    @Override
    public CompletableFuture<Boolean> deleteArena(String arena) throws ArenaNotExistException {
        CreateArena createArena = validate(arena);
        return this.deleteArena(arenaFolder, createArena).thenApply(b -> b || this.preCreation.remove(arena) != null);
    }

    private CompletableFuture<Boolean> deleteArena(File file, Arena arena) {
        return this.deleteArena(file, arena.getArenaName());
    }

    private CompletableFuture<Boolean> deleteArena(File file, String name) {
        return CompletableFuture.supplyAsync(() -> {
            File f = new File(file, name.concat(".yml"));
            if (!f.exists()) return false;
            try {
                FileUtils.forceDelete(f);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    @Override
    public void createArena(String name, Player player) throws ArenaExistException {
        if (this.preCreation.containsKey(name)) throw new ArenaExistException(name);
        CreateArena createArena = arenaMechanic.createArena(name, player);
        this.preCreation.put(name, createArena);
    }

    @Override
    public Arena[] getArenasFromWorld(World world) {
        return this.preCreation.values().stream().filter(ca -> ca.getWorld().equals(world)).toArray(Arena[]::new);
    }

    @Override
    public ImmutableList<Arena> getArenaList() {
        return ImmutableList.copyOf(preCreation.values());
    }

    @Override
    public List<String> getWarpList(String arena) throws ArenaNotExistException {
        CreateArena createArena = validate(arena);
        return new LinkedList<>(createArena.getLocationsMap().keySet());
    }

    @Override
    public CreateArena getCreateArena(String arena) throws ArenaNotExistException {
        return validate(arena);
    }

    @Override
    public void addDescriptionLine(String arena, String text) throws ArenaNotExistException {
        CreateArena createArena = validate(arena);
        createArena.addDescriptionLine(text);
    }

    @Override
    public void removeDescriptionLine(String arena) throws NoMoreElementException, ArenaNotExistException {
        CreateArena createArena = validate(arena);
        createArena.removeDescriptionLine();
    }

}

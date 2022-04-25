package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.arena.ArenaMechanic;
import com.ericlam.mc.minigames.core.arena.CreateArena;
import com.ericlam.mc.minigames.core.config.BackupConfig;
import com.ericlam.mc.minigames.core.config.MGConfig;
import com.ericlam.mc.minigames.core.event.arena.AsyncArenaLoadedEvent;
import com.ericlam.mc.minigames.core.exception.arena.ArenaNotLoadedException;
import com.ericlam.mc.minigames.core.exception.arena.FinalArenaAlreadyExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.BackupNotAllowedException;
import com.ericlam.mc.minigames.core.implement.ArenaInfo;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.world.WorldLoadedException;
import com.dragonnite.mc.dnmc.core.misc.world.WorldNonExistException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class CoreArenaManager implements ArenaManager {

    private final Location lobby;
    private final ArenaConfig config;
    private final List<Arena> arenas = new ArrayList<>();
    private final Random random = new Random();
    private final BackupConfig backupConfig;
    private final Map<String, File> randomArenas;
    private final Map<String, File> allArenas;
    private final File bukkitWorldFolder;
    @Inject
    private ArenaMechanic arenaMechanic;
    private boolean loaded = false;
    private Arena finalArena;
    @Inject
    private ArenaCreateManager arenaCreateManager;
    @Inject
    private Plugin plugin;
    private File backupFolder;

    @Inject
    public CoreArenaManager(ArenaConfig arenaConfig) {
        this.config = arenaConfig;
        this.lobby = arenaConfig.getLobbyLocation();
        this.allArenas = this.loadAllArenas();
        this.randomArenas = this.loadRandomMaxArenas(allArenas);
        var configManager = MinigamesCore.getConfigManager();
        this.backupConfig = configManager.getConfigAs(BackupConfig.class);
        if (backupConfig.arenaBackupEnabled) {
            this.backupFolder = new File(config.getArenaFolder(), "Backups");
            if (!backupFolder.exists()) backupFolder.mkdirs();
        }
        this.bukkitWorldFolder = Bukkit.getWorldContainer();
    }

    @Override
    public Arena getFinalArena() {
        return finalArena;
    }

    void setFinalArena(Arena arena) {
        this.finalArena = arena;
        if (backupConfig.arenaBackupEnabled) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    backupConfig.lastLoadedArena = arena.getArenaName();
                    backupConfig.save();
                    plugin.getLogger().info("Successfully saved last loaded arena.");
                } catch (IOException e) {
                    plugin.getLogger().warning("Error while saving last loaded arena");
                    e.printStackTrace();
                }
            });
        }
        arenas.stream().filter(a -> !a.getWorld().equals(finalArena.getWorld())).forEach(remainArena -> Bukkit.getScheduler().runTask(plugin, () -> {
            World world = remainArena.getWorld();
            try {
                boolean result = DragonNiteMC.getAPI().getWorldManager().unloadWorld(world.getName());
                plugin.getLogger().info((result ? "成功卸載" : "卸載失敗") + " 世界 " + world.getName());
            } catch (WorldNonExistException e) {
                plugin.getLogger().warning("Error: world " + e.getWorld() + " not exist, cannot unload arena " + remainArena.getArenaName());
            }
        }));
    }

    public boolean isBackedUp(Arena arena) {
        if (!backupConfig.arenaBackupEnabled || backupFolder == null) return true;
        File destination = new File(backupFolder, arena.getWorld().getName());
        return destination.exists();
    }

    public CompletableFuture<File> backupArena(Arena arena) throws BackupNotAllowedException {
        if (!backupConfig.arenaBackupEnabled || backupFolder == null)
            throw new BackupNotAllowedException(arena.getArenaName());
        return CompletableFuture.supplyAsync(() -> {
            try {
                plugin.getLogger().info("Backing up arena " + arena.getArenaName() + "...");
                if (!arena.getWorld().isAutoSave()) arena.getWorld().save();
                String worldName = arena.getWorld().getName();
                File toCopy = new File(bukkitWorldFolder, worldName);
                File destination = new File(backupFolder, worldName);
                if (destination.exists()) FileUtils.forceDelete(destination);
                FileUtils.copyDirectory(toCopy, destination);
                plugin.getLogger().info("Backup Completed");
                return destination;
            } catch (IOException e) {
                plugin.getLogger().warning("Error while backing up arena " + arena.getArenaName() + ": " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        });
    }

    private CompletableFuture<Void> restoreArena() {
        if (!backupConfig.arenaBackupEnabled || backupFolder == null) return CompletableFuture.completedFuture(null);
        return CompletableFuture.runAsync(() -> {
            var toRestore = backupConfig.lastLoadedArena;
            if (toRestore.isBlank() || toRestore.isEmpty()) {
                plugin.getLogger().info("No last loaded arena, skipped restore");
                return;
            }
            String worldName = getWorldNameFromArenaYml(toRestore);
            if (worldName == null) return;
            if (Bukkit.getWorld(worldName) != null) {
                plugin.getLogger().warning("World " + worldName + " has already been loaded, cannot restore the world.");
                return;
            }
            File toDelete = new File(bukkitWorldFolder, worldName);
            File backupWorld = new File(backupFolder, worldName);
            try {
                plugin.getLogger().info("Restoring arena " + toRestore + ", world " + worldName + " ...");
                FileUtils.forceDelete(toDelete);
                FileUtils.copyDirectory(backupWorld, toDelete);
                backupConfig.lastLoadedArena = "";
                backupConfig.save();
                plugin.getLogger().info("Restore completed.");
            } catch (IOException e) {
                plugin.getLogger().warning("Error while restoring　arena " + toRestore + ".");
                e.printStackTrace();
            }
        });
    }

    @Nullable
    private String getWorldNameFromArenaYml(String arena) {
        var file = this.allArenas.get(arena);
        if (file == null) {
            plugin.getLogger().warning("Unknown Arena " + arena + " to backup, skipped");
            return null;
        }
        String world = YamlConfiguration.loadConfiguration(file).getString("world");
        if (world == null) {
            plugin.getLogger().warning("Unknown World Name for arena " + arena + " , skipped restoring");
        }
        return world;
    }


    private CompletableFuture<PreLoadedArena> preLoadArenaFromFile(File arenaFile, String arena) {
        return CompletableFuture.supplyAsync(() -> {
            FileConfiguration yml = YamlConfiguration.loadConfiguration(arenaFile);
            String displayName = yml.getString("display-name");
            String authorName = yml.getString("author");
            String worldName = yml.getString("world");
            List<String> desc = yml.getStringList("description");
            if (worldName == null) throw new IllegalStateException("The world in arena (" + arena + ") is null");
            return new PreLoadedArena(displayName, authorName, worldName, desc, yml);
        });
    }


    private CompletableFuture<CreateArena> loadArenaFromFile(File arenaFile, String arena) {
        return this.preLoadArenaFromFile(arenaFile, arena).thenComposeAsync(preLoadedArena -> {
            try {
                return DragonNiteMC.getAPI().getWorldManager().loadWorld(preLoadedArena.worldName).thenApply(world -> {
                    if (world != null) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
                            world.setGameRule(GameRule.DO_MOB_LOOT, false);
                            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
                            world.setGameRule(GameRule.MOB_GRIEFING, false);
                            world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
                            config.setExtraWorldSetting(world);
                            if (!world.isAutoSave()) world.save();
                        });
                        preLoadedArena.loadedWorld = world;
                    }
                    return preLoadedArena;
                });
            } catch (WorldNonExistException | WorldLoadedException e) {
                plugin.getLogger().warning("Got " + e.getClass().getSimpleName() + " while loading arena " + preLoadedArena.displayName);
            }
            return CompletableFuture.completedFuture(preLoadedArena);
        }).thenApplyAsync(loadArena -> {
            if (loadArena.loadedWorld == null) {
                return null;
            }
            var yml = loadArena.yml;
            var authorName = loadArena.authorName;
            var displayName = loadArena.displayName;
            var world = loadArena.loadedWorld;
            var desc = loadArena.description;
            ConfigurationSection warpSec = yml.getConfigurationSection("warps");
            Map<String, List<Location>> listMap = new HashMap<>();
            for (String warp : Objects.requireNonNull(warpSec).getKeys(false)) {
                ConfigurationSection locSec = warpSec.getConfigurationSection(warp);
                List<Location> locations = new ArrayList<>();
                for (String index : Objects.requireNonNull(locSec).getKeys(false)) {
                    ConfigurationSection loc = locSec.getConfigurationSection(index);
                    if (loc == null) continue;
                    try {
                        locations.add(Location.deserialize(loc.getValues(true)));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().log(Level.SEVERE, e.getMessage());
                    }
                }
                listMap.put(warp, locations);
            }
            return arenaMechanic.loadCreateArena(yml, new ArenaInfo(authorName, displayName, arena, world, listMap, desc));
        });
    }

    private CompletableFuture<List<CreateArena>> loadArenasFromFile(Map<String, File> randomLoadedArenas) {
        List<CompletableFuture<Void>> createFutures = new ArrayList<>();
        List<CreateArena> arenas = new ArrayList<>();
        randomLoadedArenas.forEach((arena, file) -> createFutures.add(this.loadArenaFromFile(file, arena).thenAccept(loaded -> {
            if (loaded != null) arenas.add(loaded);
        })));
        return CompletableFuture.allOf(createFutures.toArray(CompletableFuture[]::new)).thenApply(v -> arenas);
    }

    CompletableFuture<List<Arena>> initialize() {
        this.arenas.clear();
        plugin.getLogger().info("Loading Arenas...");
        YamlManager configManager = MinigamesCore.getConfigManager();
        var launchGameOnStart = configManager.getConfigAs(MGConfig.class).lunchGameOnStart;
        if (launchGameOnStart) {
            return this.restoreArena().thenComposeAsync(v -> this.loadArenasFromFile(randomArenas))
                    .thenApply(list -> {
                        if (list.size() == 0 || lobby == null) {
                            if (list.size() == 0) plugin.getLogger().warning("No Arenas find");
                            if (lobby == null) plugin.getLogger().warning("Lobby Location is null");
                            loaded = true;
                            return List.of();
                        }
                        arenas.addAll(list);
                        loaded = true;
                        plugin.getServer().getPluginManager().callEvent(new AsyncArenaLoadedEvent(ImmutableList.copyOf(arenas)));
                        plugin.getLogger().info(list.size() + " Arenas has been successfully loaded for game start.");
                        return arenas;
                    });
        } else {
            plugin.getLogger().info("setup mode has been enabled.");
            return this.restoreArena().thenComposeAsync(v -> this.loadArenasFromFile(allArenas))
                    .thenApply(list -> {
                        ((CoreArenaCreateManager) arenaCreateManager).addAll(list);
                        plugin.getServer().getPluginManager().callEvent(new AsyncArenaLoadedEvent(ImmutableList.copyOf(arenas)));
                        plugin.getLogger().info(list.size() + " Arenas has been successfully loaded for setup.");
                        return List.of();
                    });
        }

    }

    private Map<String, File> loadRandomMaxArenas(Map<String, File> arenas) {
        var amount = Math.min(config.getMaxLoadArena(), arenas.size());
        var list = new ArrayList<>(arenas.entrySet());
        if (list.size() < 1) return Map.of();
        Map<String, File> loadedArenas = new LinkedHashMap<>();
        do {
            var entry = list.get(random.nextInt(list.size()));
            if (!loadedArenas.containsKey(entry.getKey())) loadedArenas.put(entry.getKey(), entry.getValue());
        } while (loadedArenas.size() < amount);
        return loadedArenas;
    }

    private Map<String, File> loadAllArenas() {
        var folder = config.getArenaFolder();
        var list = folder.listFiles((f, name) -> FilenameUtils.getExtension(name).equals("yml"));
        if (list == null) return Map.of();
        Map<String, File> loadedArenas = new LinkedHashMap<>();
        for (File file : list) {
            var arena = FilenameUtils.getBaseName(file.getName());
            if (!loadedArenas.containsKey(arena)) loadedArenas.put(arena, file);
        }
        return loadedArenas;
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public List<Arena> getLoadedArenas() throws ArenaNotLoadedException, FinalArenaAlreadyExistException {
        if (finalArena != null) throw new FinalArenaAlreadyExistException();
        if (!loaded) throw new ArenaNotLoadedException();
        return arenas;
    }

    private static class PreLoadedArena {
        private final String displayName;
        private final String authorName;
        private final String worldName;
        private final List<String> description;
        private final FileConfiguration yml;
        private World loadedWorld;

        public PreLoadedArena(String displayName, String authorName, String worldName, List<String> description, FileConfiguration yml) {
            this.displayName = displayName;
            this.authorName = authorName;
            this.worldName = worldName;
            this.description = description;
            this.yml = yml;
        }
    }
}

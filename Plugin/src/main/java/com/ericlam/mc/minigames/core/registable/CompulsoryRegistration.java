package com.ericlam.mc.minigames.core.registable;

import com.ericlam.mc.minigames.core.MinigamesModule;
import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.arena.ArenaMechanic;
import com.ericlam.mc.minigames.core.character.GamePlayerHandler;
import com.ericlam.mc.minigames.core.commands.arena.*;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.gamestats.GameStatsHandler;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.YamlManager;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractInventoryBuilder;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class CompulsoryRegistration implements Compulsory {

    private final MinigamesModule module;
    private final YamlManager configManager;

    private SectionTask lobbyTask;
    private SectionTask endTask;


    private boolean gamePlayerHandlerRegistered = false;
    private boolean arenaMechanicRegistered = false;
    private boolean arenaConfigRegisterd = false;
    private boolean gameStatsHandlerRegistered = false;
    private GameEntry<AbstractInventoryBuilder, List<Integer>> voteGUI;


    public CompulsoryRegistration(MinigamesModule module, YamlManager configManager) {
        this.module = module;
        this.configManager = configManager;
    }

    @Override
    public void registerLobbyTask(SectionTask task) {
        this.lobbyTask = task;
    }

    @Override
    public void registerEndTask(SectionTask task) {
        this.endTask = task;
    }

    @Override
    public void registerArenaCommand(DefaultCommand defaultCommand, JavaPlugin plugin) {
        Validate.notNull(defaultCommand, "default command is null");
        defaultCommand.addSub(new ArenaAddSpawnCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaCreateCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaCreateWarpCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaDeleteCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaInfoCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaRemoveSpawnCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaRemoveWarpCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaBackupCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaSaveCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaSetAuthorCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaSetDisplayNameCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaSetNameCommand(configManager, defaultCommand));
        defaultCommand.addSub(new SetLobbyLocationCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaAddStoryLineCommand(configManager, defaultCommand));
        defaultCommand.addSub(new ArenaRemoveStoryLineCommand(configManager, defaultCommand));
        DragoniteMC.getAPI().getCommandRegister().registerCommand(plugin, defaultCommand);
    }

    @Override
    public void registerArenaConfig(@Nonnull ArenaConfig arenaConfig) {
        Validate.notNull(arenaConfig, "Arena config is null");
        Validate.isTrue(!arenaConfig.getAllowWarps().isEmpty(), "allowed warps is empty");
        Validate.notNull(arenaConfig.getArenaFolder(), "arena folder is empty");
        this.module.register(ArenaConfig.class, arenaConfig);
        this.arenaConfigRegisterd = true;
    }

    @Override
    public void registerArenaMechanic(@Nonnull ArenaMechanic arenaMechanic) {
        Validate.notNull(arenaMechanic, "arena mechanic is null");
        this.module.register(ArenaMechanic.class, arenaMechanic);
        this.arenaMechanicRegistered = true;
    }

    @Override
    public void registerGamePlayerHandler(@Nonnull GamePlayerHandler gamePlayerHandler) {
        Validate.notNull(gamePlayerHandler, "GamePlayerHandler is null");
        this.module.register(GamePlayerHandler.class, gamePlayerHandler);
        this.gamePlayerHandlerRegistered = true;
    }

    @Override
    public void registerGameStatsHandler(@Nonnull GameStatsHandler gameStatsHandler) {
        Validate.notNull(gameStatsHandler, "GameStatsHandler is null");
        this.module.register(GameStatsHandler.class, gameStatsHandler);
        this.gameStatsHandlerRegistered = true;
    }

    @Override
    public void registerVoteGUI(AbstractInventoryBuilder inventoryBuilder, Integer... allowSlot) {
        voteGUI = new GameEntry<>(inventoryBuilder, Arrays.asList(allowSlot));
    }

    @Nullable
    public SectionTask getLobbyTask() {
        return lobbyTask;
    }

    @Nullable
    public SectionTask getEndTask() {
        return endTask;
    }

    public boolean isGamePlayerHandlerRegistered() {
        return gamePlayerHandlerRegistered;
    }

    public boolean isArenaMechanicRegistered() {
        return arenaMechanicRegistered;
    }

    public boolean isArenaConfigRegistered() {
        return arenaConfigRegisterd;
    }

    public boolean isGameStatsHandlerRegistered() {
        return gameStatsHandlerRegistered;
    }

    @Nullable
    public GameEntry<AbstractInventoryBuilder, List<Integer>> getVoteGUI() {
        return voteGUI;
    }
}

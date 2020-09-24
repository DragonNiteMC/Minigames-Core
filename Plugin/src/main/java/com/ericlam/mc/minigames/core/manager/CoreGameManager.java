package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.GameRestartRunnable;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.event.section.*;
import com.ericlam.mc.minigames.core.event.state.GameStateSwitchEvent;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.injection.GameProgramTasks;
import com.ericlam.mc.minigames.core.listeners.CrackshotListener;
import com.ericlam.mc.minigames.core.listeners.GameListener;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public final class CoreGameManager implements GameManager {

    @Inject
    private PlayerManager playerManager;
    @Inject
    private ArenaConfig arenaConfig;
    @Inject
    private ScheduleManager scheduleManager;
    @Inject
    private LobbyManager lobbyManager;
    @Inject
    private ArenaManager arenaManager;
    @Inject
    private Plugin plugin;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private GameProgramTasks gameProgramTasks;

    @Inject
    private GameItemManager gameItemManager;

    private GameState currentGameState;

    private boolean activated;

    public boolean isActivated() {
        return activated;
    }

    public void initialize() {
        if (currentGameState == GameState.STOPPED) return;
        CoreArenaManager coreArenaManager = (CoreArenaManager) arenaManager;
        coreArenaManager.initialize().whenComplete((list, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            if (list.size() == 0) {
                currentGameState = GameState.STOPPED;
                plugin.getLogger().warning("The Game will not be activated.");
                this.activated = true;
                return;
            }
            Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                ((CoreLobbyManager) lobbyManager).initialize(list);
                ((CoreInventoryManager) inventoryManager).loadVoteInventory(lobbyManager);
                ((CoreGameItemManager) gameItemManager).loadVoteItem((CoreInventoryManager) inventoryManager);
                plugin.getServer().getPluginManager().registerEvents(new GameListener((MinigamesCore) plugin), plugin);
                if (plugin.getServer().getPluginManager().getPlugin("CrackShot") != null) {
                    plugin.getServer().getPluginManager().registerEvents(new CrackshotListener((MinigamesCore) plugin), plugin);
                }
                plugin.getLogger().info("Game Activated");
                this.activated = true;
            });
        }).whenComplete((v, ex) -> Optional.ofNullable(ex).ifPresent(Throwable::printStackTrace));
    }

    @Override
    public GameState getGameState() {
        return currentGameState;
    }

    @Override
    public void endGame(List<GamePlayer> winner, GameTeam winnerTeam, boolean cancel) {
        currentGameState = GameState.PREEND;
        InGameState preEndState = gameProgramTasks.getPreEndState();
        if (!scheduleManager.isRunning(preEndState)) scheduleManager.jumpInto(preEndState, cancel);
        plugin.getServer().getPluginManager().callEvent(new GameStateSwitchEvent(getInGameState(), currentGameState, playerManager));
        GamePreEndEvent event = new GamePreEndEvent(playerManager, ImmutableList.copyOf(winner), winnerTeam, getInGameState(), currentGameState);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    @Override
    public void setState(GameState state) {
        if (state == GameState.PREEND)
            throw new UnsupportedOperationException("use endGame() method instead of setState() when reaching PRE-END");
        if (currentGameState == state) return;
        currentGameState = state;
        plugin.getServer().getPluginManager().callEvent(new GameStateSwitchEvent(getInGameState(), currentGameState, playerManager));
        GameSectionEvent sectionEvent = null;
        switch (currentGameState) {
            case VOTING:
                sectionEvent = new GameVotingEvent(playerManager, getInGameState(), currentGameState);
                break;
            case PRESTART:
                sectionEvent = new GamePreStartEvent(playerManager, getInGameState(), currentGameState);
                break;
            case IN_GAME:
                sectionEvent = new GameStartEvent(playerManager, getInGameState(), currentGameState);
                break;
            case STOPPED:
                return;
            case ENDED:
                new GameRestartRunnable(arenaManager.getFinalArena(), arenaConfig, 10).runTaskTimer(plugin, 0L, 1L);
                return;
            default:
                break;
        }
        if (sectionEvent == null) return;
        plugin.getServer().getPluginManager().callEvent(sectionEvent);
    }

    @Override
    public InGameState getInGameState() {
        return scheduleManager.getCurrentGameState();
    }

    @Override
    public String getGamePrefix() {
        return arenaConfig.getGamePrefix();
    }
}

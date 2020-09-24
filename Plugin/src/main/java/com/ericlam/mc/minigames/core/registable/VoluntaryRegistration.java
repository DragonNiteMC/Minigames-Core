package com.ericlam.mc.minigames.core.registable;

import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.implement.GlobalTeam;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public final class VoluntaryRegistration implements Voluntary {


    private final LinkedHashMap<InGameState, GameEntry<SectionTask, InGameState>> gameTasks = new LinkedHashMap<>();
    private final GlobalTeam globalTeam = new GlobalTeam();
    private final Map<Integer, ItemStack> lobbyItems = new HashMap<>();
    private final Map<GameTeam, Map<Integer, ItemStack>> gameItemMap = new HashMap<>();
    private final Map<GameTeam, Map<Integer, ItemStack>> spectatorItemMap = new HashMap<>();

    public VoluntaryRegistration() {
    }

    @Override
    public void registerGameTask(InGameState state, SectionTask task) {
        if (gameTasks.size() > 0) {
            LinkedList<InGameState> stateList = new LinkedList<>(gameTasks.keySet());
            InGameState lastState = stateList.getLast();
            gameTasks.get(lastState).setValue(state);
        }
        gameTasks.putIfAbsent(state, new GameEntry<>(task, null));
    }

    @Override
    public void addJoinItem(int slot, ItemStack item) {
        this.lobbyItems.put(slot, item);
    }

    @Override
    public void addGameItem(GameTeam team, int slot, ItemStack item) {
        this.gameItemMap.putIfAbsent(team, new HashMap<>());
        this.gameItemMap.get(team).put(slot, item);
    }

    @Override
    public void addGameItem(int slot, ItemStack item) {
        this.gameItemMap.putIfAbsent(globalTeam, new HashMap<>());
        this.gameItemMap.get(globalTeam).put(slot, item);
    }

    @Override
    public void addSpectatorItem(GameTeam team, int slot, ItemStack item) {
        this.spectatorItemMap.putIfAbsent(team, new HashMap<>());
        this.spectatorItemMap.get(team).put(slot, item);
    }

    @Override
    public void addSpectatorItem(int slot, ItemStack item) {
        this.spectatorItemMap.putIfAbsent(globalTeam, new HashMap<>());
        this.spectatorItemMap.get(globalTeam).put(slot, item);
    }

    public LinkedHashMap<InGameState, GameEntry<SectionTask, InGameState>> getGameTasks() {
        return gameTasks;
    }

    @Nonnull
    public GlobalTeam getGlobalTeam() {
        return globalTeam;
    }

    public Map<Integer, ItemStack> getLobbyItems() {
        return lobbyItems;
    }

    public Map<GameTeam, Map<Integer, ItemStack>> getGameItemMap() {
        return gameItemMap;
    }

    public Map<GameTeam, Map<Integer, ItemStack>> getSpectatorItemMap() {
        return spectatorItemMap;
    }
}

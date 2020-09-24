package com.ericlam.mc.minigames.core.injection;

import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.implement.GlobalTeam;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class GameItems {
    private final GlobalTeam globalTeam;
    private final Map<Integer, ItemStack> lobbyItems;
    private final Map<GameTeam, Map<Integer, ItemStack>> gameItemMap;
    private final Map<GameTeam, Map<Integer, ItemStack>> spectatorItemMap;

    public GameItems(GlobalTeam globalTeam, Map<Integer, ItemStack> lobbyItems, Map<GameTeam, Map<Integer, ItemStack>> gameItemMap, Map<GameTeam, Map<Integer, ItemStack>> spectatorItemMap) {
        this.globalTeam = globalTeam;
        this.lobbyItems = lobbyItems;
        this.gameItemMap = gameItemMap;
        this.spectatorItemMap = spectatorItemMap;
    }

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

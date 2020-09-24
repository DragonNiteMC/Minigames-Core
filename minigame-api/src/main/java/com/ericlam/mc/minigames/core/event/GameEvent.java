package com.ericlam.mc.minigames.core.event;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲事件
 */
public abstract class GameEvent extends Event {


    private static final HandlerList handlerList = new HandlerList();
    private InGameState inGameState;
    private GameState gameState;
    private PlayerManager playerManager;

    public GameEvent(@Nullable InGameState inGameState, @Nonnull GameState gameState, @Nonnull PlayerManager playerManager) {
        this.inGameState = inGameState;
        this.gameState = gameState;
        this.playerManager = playerManager;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * @return 場地狀態
     */
    public InGameState getInGameState() {
        return inGameState;
    }

    /**
     * @return 遊戲狀態
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @return 所有遊戲玩家
     */
    public ImmutableList<GamePlayer> getTotalPlayers() {
        return playerManager.getTotalPlayers();
    }

    /**
     * @return 所有遊戲中的玩家
     */
    public ImmutableList<GamePlayer> getGamingPlayer() {
        return playerManager.getGamePlayer();
    }

    /**
     * @return 所有投票等候的玩家
     */
    public ImmutableList<GamePlayer> getWaitingPlayer() {
        return playerManager.getWaitingPlayer();
    }

    /**
     * @return 所有觀戰的玩家
     */
    public ImmutableList<GamePlayer> getSpectators() {
        return playerManager.getSpectators();
    }


    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

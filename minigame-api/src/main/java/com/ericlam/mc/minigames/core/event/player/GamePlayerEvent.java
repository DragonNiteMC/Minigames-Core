package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.InGameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲玩家事件
 */
public abstract class GamePlayerEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private GamePlayer gamePlayer;
    private InGameState gameState;

    public GamePlayerEvent(GamePlayer gamePlayer, @Nullable InGameState state) {
        this.gamePlayer = gamePlayer;
        this.gameState = state;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * @return 牽涉的遊戲玩家
     */
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    /**
     * @return 場地狀態
     */
    @Nullable
    public InGameState getInGameState() {
        return gameState;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

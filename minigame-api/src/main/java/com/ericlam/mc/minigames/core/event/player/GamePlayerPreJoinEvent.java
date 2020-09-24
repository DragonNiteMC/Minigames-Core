package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲玩家準備加入時觸發的事件。
 * <p>
 * 此事件可以被取消。當取消事件後，小遊戲系統將不會把該玩家加入為遊戲玩家。
 */
public final class GamePlayerPreJoinEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handerlist = new HandlerList();
    private boolean cancelled;
    private GameState state;
    private InGameState inGameState;

    public GamePlayerPreJoinEvent(Player who, GameState state, @Nullable InGameState inGameState) {
        super(who);
        this.cancelled = false;
        this.state = state;
        this.inGameState = inGameState;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handerlist;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    /**
     * 獲取遊戲狀態
     *
     * @return 遊戲狀態
     */
    public GameState getState() {
        return state;
    }

    /**
     * 獲取場地狀態
     *
     * @return 場地狀態
     */
    @Nullable
    public InGameState getInGameState() {
        return inGameState;
    }
}

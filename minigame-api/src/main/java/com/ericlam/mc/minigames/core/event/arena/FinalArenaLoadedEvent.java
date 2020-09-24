package com.ericlam.mc.minigames.core.event.arena;

import com.ericlam.mc.minigames.core.arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * 投票結果選出最終場地時觸發的事件
 */
public final class FinalArenaLoadedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Arena finalArena;

    public FinalArenaLoadedEvent(@Nonnull Arena finalArena) {
        this.finalArena = finalArena;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * 獲取投票結果選出來的場地
     *
     * @return 最終場地
     */
    public Arena getFinalArena() {
        return finalArena;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

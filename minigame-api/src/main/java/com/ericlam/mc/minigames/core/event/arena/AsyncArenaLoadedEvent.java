package com.ericlam.mc.minigames.core.event.arena;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.google.common.collect.ImmutableList;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * 場地加載時觸發的異步事件。
 */
public final class AsyncArenaLoadedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final ImmutableList<Arena> loadedArenas;

    public AsyncArenaLoadedEvent(ImmutableList<Arena> loadedArenas) {
        super(true);
        this.loadedArenas = loadedArenas;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * @return 被加載的場地
     */
    public ImmutableList<Arena> getLoadedArenas() {
        return loadedArenas;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

package com.ericlam.mc.minigames.core.exception.arena;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.exception.GameRunException;

public class UnknownArenaException extends GameRunException {

    public UnknownArenaException(Arena arena) {
        super("Unknown Arena whilst voting: " + arena.getArenaName(), false);
    }
}

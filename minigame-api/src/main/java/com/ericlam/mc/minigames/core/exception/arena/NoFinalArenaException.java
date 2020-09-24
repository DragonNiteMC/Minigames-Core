package com.ericlam.mc.minigames.core.exception.arena;

import com.ericlam.mc.minigames.core.exception.GameRunException;

public class NoFinalArenaException extends GameRunException {

    public NoFinalArenaException() {
        super("There is no final arena in result", true);
    }
}

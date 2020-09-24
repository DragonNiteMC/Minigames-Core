package com.ericlam.mc.minigames.core.exception.arena;

import com.ericlam.mc.minigames.core.exception.GameRunException;

public class ArenaNotLoadedException extends GameRunException {

    public ArenaNotLoadedException() {
        super("Arenas has not loaded", true);
    }
}

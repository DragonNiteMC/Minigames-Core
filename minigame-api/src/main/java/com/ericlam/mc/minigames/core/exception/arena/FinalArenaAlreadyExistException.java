package com.ericlam.mc.minigames.core.exception.arena;

import com.ericlam.mc.minigames.core.exception.GameRunException;

public class FinalArenaAlreadyExistException extends GameRunException {

    public FinalArenaAlreadyExistException() {
        super("Final Arena already exist", false);
    }
}

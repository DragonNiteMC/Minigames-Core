package com.ericlam.mc.minigames.core.exception;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaCreateException;

public class NoMoreElementException extends ArenaCreateException {

    public NoMoreElementException(String arena) {
        super(arena);
    }
}

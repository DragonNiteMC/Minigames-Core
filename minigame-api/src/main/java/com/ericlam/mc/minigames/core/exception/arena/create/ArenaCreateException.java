package com.ericlam.mc.minigames.core.exception.arena.create;

public abstract class ArenaCreateException extends Exception {

    private String arena;

    public ArenaCreateException(String arena) {
        this.arena = arena;
    }

    public String getArena() {
        return arena;
    }
}

package com.ericlam.mc.minigames.core.exception.arena.create;

public class WarpException extends Exception {
    private String warp;

    public WarpException(String warp) {
        this.warp = warp;
    }

    public String getWarp() {
        return warp;
    }
}

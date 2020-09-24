package com.ericlam.mc.minigames.core.exception.arena.create;

public class LocationMaxReachedException extends WarpException {
    public LocationMaxReachedException(String warp) {
        super(warp);
    }
}

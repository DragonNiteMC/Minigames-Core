package com.ericlam.mc.minigames.core.exception.arena.create;

public class IllegalWarpException extends WarpException {

    private String[] allowWarps;

    public IllegalWarpException(String warp, String... allowWarps) {
        super(warp);
        this.allowWarps = allowWarps;
    }

    public String[] getAllowWarps() {
        return allowWarps;
    }
}

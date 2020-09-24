package com.ericlam.mc.minigames.core.exception;

import org.bukkit.Bukkit;

public class GameRunException extends RuntimeException {

    private boolean shutdown;

    public GameRunException(String errorMessage, boolean shutdown) {
        super(errorMessage);
        this.shutdown = shutdown;
    }

    @Override
    public synchronized Throwable getCause() {
        if (shutdown) Bukkit.getServer().shutdown();
        return super.getCause();
    }
}

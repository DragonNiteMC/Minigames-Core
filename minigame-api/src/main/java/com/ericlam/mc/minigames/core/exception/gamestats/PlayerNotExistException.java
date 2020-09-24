package com.ericlam.mc.minigames.core.exception.gamestats;

import org.bukkit.OfflinePlayer;

public final class PlayerNotExistException extends Exception {
    private OfflinePlayer player;

    public PlayerNotExistException(OfflinePlayer gamePlayer) {
        this.player = gamePlayer;
    }

    public OfflinePlayer getGamePlayer() {
        return player;
    }
}

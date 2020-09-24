package com.ericlam.mc.minigames.core.game;

import net.md_5.bungee.api.ChatColor;

/**
 * 遊戲狀態
 */
public enum GameState {

    /**
     * 投票中
     */
    VOTING("&a等待中"),

    /**
     * 即將開始
     */
    PRESTART("&b遊戲即將開始"),

    /**
     * 遊戲中
     */
    IN_GAME("&b遊戲中"),

    /**
     * 邁向完結
     */
    PREEND("&c遊戲邁向完結"),

    /**
     * 遊戲完結; 設置這個狀態後將會直接重啟伺服器
     */
    ENDED("&c遊戲已完結"),

    /**
     * 遊戲沒有被激活
     */
    STOPPED(null);

    private final String motd;

    GameState(final String motd) {
        this.motd = motd;
    }

    /**
     * 獲取 motd
     *
     * @return motd
     */
    public String getMotd() {
        return ChatColor.translateAlternateColorCodes('&', motd);
    }

}

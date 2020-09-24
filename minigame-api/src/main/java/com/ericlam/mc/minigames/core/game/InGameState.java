package com.ericlam.mc.minigames.core.game;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 場地狀態容器
 */
public class InGameState {

    private final String stateName;
    private final String motd;

    /**
     * @param stateName 狀態名稱
     * @param motd      該狀態時顯示的 motd, 支援顏色
     */
    public InGameState(@Nonnull String stateName, @Nullable String motd) {
        this.stateName = stateName;
        this.motd = motd;
    }

    /**
     * @return 狀態名稱
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * @return 該狀態時顯示的 motd
     */
    @Nullable
    public String getMotd() {
        return motd == null ? null : ChatColor.translateAlternateColorCodes('&', motd);
    }

}

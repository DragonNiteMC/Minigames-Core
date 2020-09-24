package com.ericlam.mc.minigames.core.game;

import net.md_5.bungee.api.ChatColor;

/**
 * 可設置的隊伍容器
 */
public interface VariableTeam extends GameTeam {

    /**
     * 設置隊伍名稱
     *
     * @param teamName 新名稱
     */
    void setTeamName(String teamName);

    /**
     * 設置隊伍顏色
     *
     * @param color 新顏色
     */
    void setColor(ChatColor color);

    /**
     * 設置隊伍開啟隊友傷害
     *
     * @param enabled 啟用
     */
    void setEnabledFriendlyFire(boolean enabled);

}

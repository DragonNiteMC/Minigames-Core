package com.ericlam.mc.minigames.core.game;

import com.ericlam.mc.minigames.core.function.Castable;
import org.bukkit.ChatColor;

/**
 * 隊伍容器接口
 */
public interface GameTeam extends Castable<GameTeam> {

    /**
     * 獲取隊伍名稱
     *
     * @return 隊伍名稱
     */
    String getTeamName();

    /**
     * 獲取隊伍顏色
     *
     * @return 隊伍顏色
     */
    ChatColor getColor();

    /**
     * @return 是否開啟隊友傷害
     */
    boolean isEnabledFriendlyFire();

}

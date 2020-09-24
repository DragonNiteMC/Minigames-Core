package com.ericlam.mc.minigames.core.factory.compass;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.GameTeam;

/**
 * 由 {@link CompassFactory} 創建的羅盤追蹤器
 */
public interface CompassTracker {

    /**
     * 設置個人目標
     *
     * @param player 玩家
     * @param team   目標隊伍
     */
    void setIndividualTarget(GamePlayer player, GameTeam team);

    /**
     * 啟動追蹤器
     */
    void launch();

    /**
     * 銷毀追蹤器，此動作無法復原
     */
    void destroy();

}

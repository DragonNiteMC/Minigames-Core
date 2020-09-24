package com.ericlam.mc.minigames.core.factory;

import com.ericlam.mc.minigames.core.factory.compass.CompassFactory;
import com.ericlam.mc.minigames.core.factory.scoreboard.ScoreboardFactory;

/**
 * 遊戲工廠
 */
public interface GameFactory {

    /**
     * 創建計分版工廠
     *
     * @return 計分版工廠
     */
    ScoreboardFactory getScoreboardFactory();

    /**
     * 創建羅盤追蹤器工廠
     *
     * @return 羅盤追蹤器工廠
     */
    CompassFactory getCompassFactory();

}

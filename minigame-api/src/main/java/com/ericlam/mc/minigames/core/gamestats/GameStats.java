package com.ericlam.mc.minigames.core.gamestats;

import com.ericlam.mc.minigames.core.function.Castable;

/**
 * 遊戲玩家資料容器接口
 */
public interface GameStats extends Castable<GameStats> {

    /**
     * @return 遊玩次數
     */
    int getPlayed();

    /**
     * @return 殺數
     */
    int getKills();

    /**
     * @return 死數
     */
    int getDeaths();

    /**
     * @return 勝數
     */
    int getWins();

    /**
     * @return 分數
     */
    double getScores();

    /**
     * 資料資訊顯示
     *
     * @return 遊戲玩家資訊
     */
    String[] getInfo();

}

package com.ericlam.mc.minigames.core.gamestats;

import com.ericlam.mc.minigames.core.function.Castable;

/**
 * 遊戲玩家資料容器接口
 */
public interface GameStats extends Castable<GameStats>, Cloneable {

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

    /**
     * 複製戰績，用於使用 {@link #minus(GameStats)} 計算
     * @return 複製後的戰績
     */
    GameStats clone();

    /**
     * 計算與原本戰績的差距，用於作為 Record 儲存
     * @param original 原本戰績
     * @return 計算後的 Record
     */
    GameStats minus(GameStats original);

}

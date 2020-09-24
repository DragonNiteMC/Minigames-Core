package com.ericlam.mc.minigames.core.gamestats;

/**
 * 可編輯的遊戲玩家資料容器接口
 */
public interface GameStatsEditor extends GameStats {

    /**
     * 設置殺敵數
     *
     * @param kills 殺數
     */
    void setKills(int kills);

    /**
     * 設置死亡數
     *
     * @param deaths 死數
     */
    void setDeaths(int deaths);

    /**
     * 設置遊玩次數
     *
     * @param played 遊玩次數
     */
    void setPlayed(int played);

    /**
     * 設置勝利數
     *
     * @param wins 勝數
     */
    void setWins(int wins);


    /**
     * 設置分數
     *
     * @param scores 分數
     */
    void setScores(double scores);

}

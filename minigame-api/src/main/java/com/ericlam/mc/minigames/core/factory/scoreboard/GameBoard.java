package com.ericlam.mc.minigames.core.factory.scoreboard;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.TeamPlayer;
import com.google.common.collect.ImmutableMap;

/**
 * 被 {@link ScoreboardFactory} 創建的 計分版。
 */
public interface GameBoard {

    /**
     * 設置計分版標題
     *
     * @param title 標題
     */
    void setTitle(String title);

    /**
     * 添加玩家到計分版
     *
     * @param player 遊戲玩家
     */
    void addPlayer(GamePlayer player);

    /**
     * 根據玩家目前狀態切換計分版隊伍
     *
     * @param player 隊伍玩家
     */
    void switchTeam(TeamPlayer player);

    /**
     * 在計分版移除玩家
     *
     * @param player 遊戲玩家
     */
    void removePlayer(GamePlayer player);

    /**
     * 獲取計分版目前內容
     *
     * @return 計分版內容 文字 + 分數
     */
    ImmutableMap<String, Integer> getSidebarLine();

    /**
     * 設置計分版內特定行的分數
     *
     * @param key   標識文字
     * @param score 分數
     */
    void setScore(String key, int score);

    /**
     * 設置計分版內特定行的文字內容
     *
     * @param key  標識文字
     * @param line 新文字內容, 支援顏色
     */
    void setLine(String key, String line);

    /**
     * 設置計分版內特定行的文字內容和分數
     *
     * @param key   標識文字
     * @param line  新文字內容，支援顏色
     * @param score 新分數
     */
    void setLine(String key, String line, int score);

    /**
     * 更新特定玩家的個人所屬顯示
     *
     * @param player 玩家
     */
    void updateIndividual(GamePlayer player);

    /**
     * 更新所有玩家的個人所屬顯示
     */
    void updateIndividual();

    /**
     * 銷毀計分版，此動作將無法復原。
     */
    void destroy();
}

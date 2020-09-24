package com.ericlam.mc.minigames.core.character;

import org.bukkit.entity.Player;

/**
 * 讓使用者註冊的遊戲玩家處理機制。
 */
public interface GamePlayerHandler {

    /**
     * 當玩家的狀態被改變時的事件處理
     *
     * @param player 被改變狀態的玩家
     * @param status 已改變的狀態
     */
    void onPlayerStatusChange(GamePlayer player, GamePlayer.Status status);

    /**
     * 當玩家被小遊戲系統移除的事件處理
     *
     * @param player 被移除的玩家
     */
    void onPlayerRemove(GamePlayer player);

    /**
     * 定義如何創建遊戲玩家
     *
     * @param player 玩家實體
     * @return 遊戲玩家的容器
     */
    GamePlayer createGamePlayer(Player player);

    /**
     * @return 最少開始遊戲人數
     */
    int requireStart();

}

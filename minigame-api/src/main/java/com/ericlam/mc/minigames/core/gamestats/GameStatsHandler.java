package com.ericlam.mc.minigames.core.gamestats;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 遊戲玩家資料處理機制接口
 */
public interface GameStatsHandler {

    /**
     * 定義如何加載遊戲玩家資料
     *
     * @param player 玩家
     * @return 遊戲玩家資料實作容器
     */
    @Nonnull
    GameStatsEditor loadGameStatsData(@Nonnull Player player);

    /**
     * 定義如何保存遊戲玩家資料
     *
     * @param player    玩家
     * @param gameStats 遊戲玩家資料
     * @return 異步運行
     */
    CompletableFuture<Void> saveGameStatsData(OfflinePlayer player, GameStats gameStats);

    /**
     * 定義如何保存所有遊戲玩家資料
     *
     * @param gameStatsMap 遊戲玩家資料列表
     * @return 異步運行
     */
    CompletableFuture<Void> saveGameStatsData(Map<OfflinePlayer, GameStats> gameStatsMap);


    /**
     * 定義如何保存遊戲記錄
     * @param player 玩家
     * @param stats 遊戲玩家資料
     * @param timeStamp 遊戲時間戳記
     * @return 異步運行
     */
    CompletableFuture<Void> saveGameStatsRecord(OfflinePlayer player, GameStats stats, Timestamp timeStamp);

    /**
     * 定義如何保存所有遊戲玩家記錄
     * @param gameStatsMap 遊戲玩家資料列表
     * @param timeStampMap 遊戲時間戳記列表
     * @return 異步運行
     */
    CompletableFuture<Void> saveGameStatsRecord(Map<OfflinePlayer, GameStats> gameStatsMap, Map<OfflinePlayer, Timestamp> timeStampMap);

}

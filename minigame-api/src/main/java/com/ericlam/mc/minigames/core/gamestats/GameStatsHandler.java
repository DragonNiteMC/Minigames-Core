package com.ericlam.mc.minigames.core.gamestats;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
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

}

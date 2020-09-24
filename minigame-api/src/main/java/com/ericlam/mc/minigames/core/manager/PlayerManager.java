package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * 遊戲玩家管理器
 */
public interface PlayerManager {

    /**
     * 獲取所有遊戲玩家
     *
     * @return 所有遊戲玩家
     */
    ImmutableList<GamePlayer> getTotalPlayers();

    /**
     * 獲取所有觀戰者
     *
     * @return 所有觀戰者
     */
    ImmutableList<GamePlayer> getSpectators();

    /**
     * 獲取所有遊戲中的玩家
     *
     * @return 所有遊戲中的玩家
     */
    ImmutableList<GamePlayer> getGamePlayer();

    /**
     * 設置為遊戲中的玩家
     *
     * @param player 玩家
     */
    void setGamePlayer(GamePlayer player);

    /**
     * 獲取所有等待投票的玩家
     *
     * @return 所有等待投票的玩家
     */
    ImmutableList<GamePlayer> getWaitingPlayer();

    /**
     * 設置為等待中的玩家
     *
     * @param player 玩家
     */
    void setWaitingPlayer(GamePlayer player);

    /**
     * 尋找遊戲玩家
     *
     * @param player 玩家
     * @return 可能為 null 的遊戲玩家
     */
    Optional<GamePlayer> findPlayer(Player player);

    /**
     * 創建遊戲玩家
     *
     * @param player 玩家
     * @return 遊戲玩家
     */
    GamePlayer buildGamePlayer(Player player);

    /**
     * 設置為觀戰者
     *
     * @param player 玩家
     */
    void setSpectator(GamePlayer player);

    /**
     * 是否可以開始進行倒數
     *
     * @return 可以開始進行倒數
     */
    boolean shouldStart();

    /**
     * 刪除遊戲玩家
     *
     * @param player 遊戲玩家
     */
    void removePlayer(GamePlayer player);

}

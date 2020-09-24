package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.exception.gamestats.PlayerNotExistException;
import com.ericlam.mc.minigames.core.gamestats.GameStatsEditor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * 遊戲玩家資料管理器
 */
public interface GameStatsManager {

    /**
     * 獲取遊戲玩家資訊
     *
     * @param player 遊戲玩家
     * @return 遊戲玩家資訊
     * @throws PlayerNotExistException 遊戲玩家不存在時
     */
    String[] getStatsInfo(GamePlayer player) throws PlayerNotExistException;

    /**
     * 獲取遊戲玩家資訊
     *
     * @param player 玩家
     * @return 遊戲玩家資訊
     * @throws PlayerNotExistException 遊戲玩家不存在時
     */
    String[] getStatsInfo(Player player) throws PlayerNotExistException;

    /**
     * 增加殺數
     *
     * @param player 玩家
     * @param kills  增加
     */
    void addKills(GamePlayer player, int kills);

    /**
     * 減少玩家殺數
     *
     * @param player 玩家
     * @param kills  減少
     */
    void minusKills(GamePlayer player, int kills);

    /**
     * 增加玩家死數
     *
     * @param player 玩家
     * @param deaths 增加
     */
    void addDeaths(GamePlayer player, int deaths);

    /**
     * 減少玩家死數
     *
     * @param player 玩家
     * @param deaths 減少
     */
    void minusDeaths(GamePlayer player, int deaths);

    /**
     * 增加玩家勝數
     *
     * @param player 玩家
     * @param wins   增加
     */
    void addWins(GamePlayer player, int wins);

    /**
     * 減少玩家勝數
     *
     * @param player 玩家
     * @param wins   減少
     */
    void minusWins(GamePlayer player, int wins);

    /**
     * 增加玩家遊玩次數
     *
     * @param player 玩家
     * @param played 增加
     */
    void addPlayed(GamePlayer player, int played);

    /**
     * 減少玩家遊玩次數
     *
     * @param player 玩家
     * @param played 減少
     */
    void minusPlayed(GamePlayer player, int played);

    /**
     * 新增分數
     *
     * @param player 玩家
     * @param scores 分數
     */
    void addScores(GamePlayer player, double scores);

    /**
     * 減少分數
     *
     * @param player 玩家
     * @param scores 分數
     */
    void minusScores(GamePlayer player, double scores);

    /**
     * 獲取 遊戲玩家資訊的 可編輯容器
     *
     * @param player 玩家
     * @return 遊戲玩家資訊
     * @throws PlayerNotExistException 玩家不存在時
     */
    GameStatsEditor getGameStats(GamePlayer player) throws PlayerNotExistException;

    /**
     * 加載該玩家的遊戲資料
     *
     * @param player 玩家
     * @return 異步運行
     */
    CompletableFuture<Boolean> loadGameStats(GamePlayer player);

    /**
     * 儲存所有玩家的遊戲資料
     *
     * @return 異步運行
     */
    CompletableFuture<Void> saveAll();

    /**
     * 保存單獨玩家的遊戲資料
     *
     * @param player 玩家
     * @return 異步運行
     * @throws PlayerNotExistException 玩家不存在時
     */
    CompletableFuture<Void> savePlayer(OfflinePlayer player) throws PlayerNotExistException;

}

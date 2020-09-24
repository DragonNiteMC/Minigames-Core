package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.exception.AlreadyVotedException;
import com.ericlam.mc.minigames.core.exception.arena.NoFinalArenaException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * 等候大堂管理器
 */
public interface LobbyManager {

    /**
     * 傳送到大堂位置
     *
     * @param player 玩家
     */
    void tpLobbySpawn(Player player);

    /**
     * 獲取可投票場地
     *
     * @return 可投票場地
     */
    ImmutableList<Arena> getCandidate();

    /**
     * 獲取該玩家投票的場地
     *
     * @param player 玩家
     * @return 可能為 null 的 投票場地
     */
    Optional<Arena> getVoted(GamePlayer player);

    /**
     * 玩家投票
     *
     * @param player 玩家
     * @param arena  投票場地
     * @throws AlreadyVotedException 已經投票了相同場地時
     */
    void vote(GamePlayer player, Arena arena) throws AlreadyVotedException;

    /**
     * 取消玩家投票
     *
     * @param player 玩家
     */
    void unVote(GamePlayer player);

    /**
     * 獲取目前投票結果
     *
     * @return 投票結果
     */
    ImmutableMap<Arena, ImmutableList<GamePlayer>> getResult();

    /**
     * 運行投票結果計算出最終場地
     *
     * @throws NoFinalArenaException 找不到最終場地時
     */
    void runFinalResult() throws NoFinalArenaException;

}

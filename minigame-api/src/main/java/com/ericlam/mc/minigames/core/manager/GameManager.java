package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.game.InGameState;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 遊戲主體管理器
 */
public interface GameManager {

    /**
     * 獲取目前遊戲狀態
     *
     * @return 遊戲狀態
     */
    GameState getGameState();

    /**
     * 直接結束遊戲
     *
     * @param winner     勝利者
     * @param winnerTeam 勝利隊伍
     * @param cancel     取消原定程序, 若果為 false 則會強制完成程序
     */
    void endGame(List<GamePlayer> winner, GameTeam winnerTeam, boolean cancel);

    /**
     * 設置遊戲狀態
     * <p>
     * 注意你無法設置為 {@link GameState#PREEND}, 你只能使用 {@link #endGame(List, GameTeam, boolean)} 來設置。
     * </p>
     *
     * @param state 遊戲狀態
     */
    void setState(GameState state);

    /**
     * 獲取場地狀態
     *
     * @return 場地狀態
     */
    @Nullable
    InGameState getInGameState();

    /**
     * 獲取遊戲前綴
     *
     * @return 遊戲前綴
     */
    String getGamePrefix();
}

package com.ericlam.mc.minigames.core;

import com.ericlam.mc.minigames.core.manager.*;

/**
 * 小遊戲 API
 */
public interface MinigamesAPI {

    /**
     * 獲取煙火管理器
     *
     * @return 煙火管理器
     */
    FireWorkManager getFireWorkManager();

    /**
     * 獲取場地管理器
     *
     * @return 場地管理器
     */
    ArenaManager getArenaManager();

    /**
     * 獲取等候大堂管理器
     *
     * @return 等候大堂管理器
     */
    LobbyManager getLobbyManager();

    /**
     * 獲取排程管理器
     *
     * @return 排程管理器
     */
    ScheduleManager getScheduleManager();

    /**
     * 獲取遊戲物品管理器
     *
     * @return 遊戲物品管理器
     */
    GameItemManager getGameItemManager();

    /**
     * 獲取場地設置管理器
     *
     * @return 場地設置管理器
     */
    ArenaCreateManager getArenaCreateManager();

    /**
     * 獲取遊戲玩家資料管理器
     *
     * @return 遊戲玩家資料管理器
     */
    GameStatsManager getGameStatsManager();

    /**
     * 獲取遊戲玩家管理器
     *
     * @return 遊戲玩家管理器
     */
    PlayerManager getPlayerManager();

    /**
     * 獲取遊戲主體管理器
     *
     * @return 遊戲主體管理器
     */
    GameManager getGameManager();

    /**
     * 獲取遊戲內工具類
     *
     * @return 遊戲內工具類
     */
    GameUtils getGameUtils();

}

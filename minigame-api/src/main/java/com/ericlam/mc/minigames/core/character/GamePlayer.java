package com.ericlam.mc.minigames.core.character;

import com.ericlam.mc.minigames.core.function.Castable;
import org.bukkit.entity.Player;

/**
 * 代表遊戲玩家的接口, 包括觀戰/遊戲/投票中的玩家。
 */
public interface GamePlayer extends Castable<GamePlayer> {

    /**
     * @return 真正的玩家實體
     */
    Player getPlayer();

    /**
     * @return 該玩家的狀態
     */
    Status getStatus();

    /**
     * 設置該玩家的狀態
     *
     * @param status 狀態
     */
    void setStatus(Status status);

    /**
     * 玩家狀態
     */
    enum Status {
        /**
         * 遊戲中
         */
        GAMING,
        /**
         * 大堂投票中
         */
        WAITING,
        /**
         * 觀戰中
         */
        SPECTATING;
    }

}

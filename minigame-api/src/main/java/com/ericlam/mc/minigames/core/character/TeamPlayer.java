package com.ericlam.mc.minigames.core.character;

import com.ericlam.mc.minigames.core.game.GameTeam;

/**
 * 擁有隊伍的遊戲玩家專用容器。
 */
public interface TeamPlayer extends GamePlayer {

    /**
     * @return 所屬隊伍
     */
    GameTeam getTeam();

    /**
     * 設置隊伍
     *
     * @param team 隊伍
     */
    void setTeam(GameTeam team);

}

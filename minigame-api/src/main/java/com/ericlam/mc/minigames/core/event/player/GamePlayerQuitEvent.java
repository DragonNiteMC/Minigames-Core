package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;

/**
 * 遊戲玩家退出事件
 */
public class GamePlayerQuitEvent extends GamePlayerEvent {

    private GameState gameState;

    public GamePlayerQuitEvent(GamePlayer gamePlayer, InGameState state, GameState gameState) {
        super(gamePlayer, state);
        this.gameState = gameState;
    }


    /**
     * @return 遊戲狀態
     */
    public GameState getGameState() {
        return gameState;
    }


}

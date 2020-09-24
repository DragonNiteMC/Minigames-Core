package com.ericlam.mc.minigames.core.event.state;

import com.ericlam.mc.minigames.core.event.GameEvent;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.manager.PlayerManager;

/**
 * 場地狀態切換事件
 */
public final class InGameStateSwitchEvent extends GameEvent {

    public InGameStateSwitchEvent(InGameState inGameState, GameState gameState, PlayerManager playerManager) {
        super(inGameState, gameState, playerManager);
    }
}

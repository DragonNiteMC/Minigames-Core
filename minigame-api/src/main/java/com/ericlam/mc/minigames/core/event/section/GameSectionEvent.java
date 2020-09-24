package com.ericlam.mc.minigames.core.event.section;

import com.ericlam.mc.minigames.core.event.GameEvent;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.manager.PlayerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲階段事件
 */
public abstract class GameSectionEvent extends GameEvent {

    public GameSectionEvent(@Nonnull PlayerManager playerManager, @Nullable InGameState inGameState, GameState gameState) {
        super(inGameState, gameState, playerManager);
    }

}

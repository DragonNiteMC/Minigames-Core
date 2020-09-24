package com.ericlam.mc.minigames.core.event.section;

import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.manager.PlayerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲開始事件，遊戲開始時被觸發。
 * <p>
 * 它前一個階段是 {@link GamePreStartEvent}
 */
public final class GameStartEvent extends GameSectionEvent {

    public GameStartEvent(@Nonnull PlayerManager playerManager, @Nullable InGameState inGameState, GameState gameState) {
        super(playerManager, inGameState, gameState);
    }
}

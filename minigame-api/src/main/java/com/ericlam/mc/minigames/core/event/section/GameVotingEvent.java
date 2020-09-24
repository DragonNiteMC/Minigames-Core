package com.ericlam.mc.minigames.core.event.section;

import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.manager.PlayerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲等候投票事件。他是最初始的遊戲事件, 在遊戲被激活并可以進入的時候觸發。
 */
public final class GameVotingEvent extends GameSectionEvent {
    public GameVotingEvent(@Nonnull PlayerManager playerManager, @Nullable InGameState inGameState, GameState gameState) {
        super(playerManager, inGameState, gameState);
    }
}

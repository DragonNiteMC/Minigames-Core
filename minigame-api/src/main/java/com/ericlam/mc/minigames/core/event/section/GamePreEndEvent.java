package com.ericlam.mc.minigames.core.event.section;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 遊戲結束事件，當遊戲結束後被觸發
 */
public final class GamePreEndEvent extends GameSectionEvent {

    private final ImmutableList<GamePlayer> winners;

    private final GameTeam winnerTeam;

    public GamePreEndEvent(@Nonnull PlayerManager playerManager, ImmutableList<GamePlayer> winners, @Nullable GameTeam winnerTeam, @Nullable InGameState inGameState, GameState gameState) {
        super(playerManager, inGameState, gameState);
        this.winners = winners;
        this.winnerTeam = winnerTeam;
    }

    /**
     * 獲取勝利者
     *
     * @return 勝利者
     */
    public ImmutableList<GamePlayer> getWinners() {
        return winners;
    }

    /**
     * 獲取勝利隊伍，若果沒有隊伍，則為 null
     *
     * @return 勝利隊伍
     */
    @Nullable
    public GameTeam getWinnerTeam() {
        return winnerTeam;
    }
}

package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import org.bukkit.event.Cancellable;

import javax.annotation.Nonnull;

/**
 * 遊戲玩家投票地圖事件
 * <p>
 * 此事件可以被取消，當事件被取消後，投票數將不會生效。
 */
public final class GamePlayerVoteEvent extends GamePlayerEvent implements Cancellable {

    private Arena voted;
    private boolean cancel;

    public GamePlayerVoteEvent(@Nonnull Arena voted, GamePlayer gamePlayer) {
        super(gamePlayer, null);
        this.voted = voted;
        this.cancel = false;
    }

    /**
     * 獲取投票的地圖
     *
     * @return 投票地圖
     */
    public Arena getVoted() {
        return voted;
    }


    /**
     * 設置投票的地圖
     *
     * @param voted 投票地圖
     */
    public void setVoted(Arena voted) {
        this.voted = voted;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}

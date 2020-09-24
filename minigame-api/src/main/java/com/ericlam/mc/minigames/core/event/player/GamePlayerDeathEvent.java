package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.InGameState;

import javax.annotation.Nullable;

/**
 * 遊戲玩家死亡事件
 */
public class GamePlayerDeathEvent extends GamePlayerEvent {

    private final GamePlayer killer;
    private final DeathCause deathCause;
    private final String action;

    public GamePlayerDeathEvent(@Nullable GamePlayer killer, GamePlayer gamePlayer, DeathCause deathCause, InGameState state, String action) {
        super(gamePlayer, state);
        this.killer = killer;
        this.deathCause = deathCause;
        this.action = action;
    }

    /**
     * @return 兇手
     */
    @Nullable
    public GamePlayer getKiller() {
        return killer;
    }

    /**
     * @return 死亡原因
     * @see DeathCause
     */
    public DeathCause getDeathCause() {
        return deathCause;
    }


    /**
     * 如何死亡
     * <p>
     * 例如, `燒死了`, `淹死了` 等等
     *
     * @return 如何死亡的訊息
     */
    public String getAction() {
        return action;
    }

    /**
     * 死亡原因
     */
    public enum DeathCause {

        /**
         * 槍械射死
         *
         * @see CrackShotDeathEvent
         */
        CRACKSHOT,

        /**
         * 普通傷害
         */
        BUKKIT_DAMAGE,

        /**
         * 普通殺害
         */
        BUKKIT_KILL,

        /**
         * 普通死亡
         */
        BUKKIT_DEATH,

        /**
         * 自定義
         */
        CUSTOM;

        private String value;

        /**
         * 創建自定義死亡的類型
         *
         * @param value 死亡原因
         * @return 死亡類型
         */
        public static DeathCause ofCustom(String value) {
            CUSTOM.setValue(value);
            return CUSTOM;
        }

        /**
         * 獲取自定義死亡原因
         *
         * @return 死亡原因
         */
        public String getValue() {
            return this == CUSTOM ? value : this.toString().toLowerCase();
        }

        /**
         * 設置自定義死亡原因
         *
         * @param value 死亡原因
         */
        private void setValue(String value) {
            this.value = value;
        }
    }
}

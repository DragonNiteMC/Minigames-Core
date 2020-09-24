package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 因 CrackShot 而死亡的遊戲玩家事件
 */
public final class CrackShotDeathEvent extends GamePlayerDeathEvent {

    private String weaponTitle;
    private Entity bullet;
    private Set<DamageType> types;

    public CrackShotDeathEvent(@Nullable GamePlayer killer, GamePlayer gamePlayer, InGameState state, String weaponTitle, Entity bullet, Set<DamageType> types) {
        super(killer, gamePlayer, DeathCause.CRACKSHOT, state, MinigamesCore.getProperties().getMessageGetter().getPure("death-msg.action.".concat(bullet == null ? "normal" : "gun")));
        this.weaponTitle = weaponTitle;
        this.bullet = bullet;
        this.types = types;
    }

    /**
     * @return 傷害類型
     * @see DamageType
     */
    public Set<DamageType> getDamageTypes() {
        return types;
    }

    /**
     * 返回該槍械的 CrackShot Title
     *
     * @return 該槍械的 CrackShot Title
     */
    public String getWeaponTitle() {
        return weaponTitle;
    }

    /**
     * 返回子彈實體
     *
     * @return 子彈
     */
    public Entity getBullet() {
        return bullet;
    }

    /**
     * 傷害類型
     */
    public enum DamageType {
        /**
         * 後刺
         */
        BACKSTAB,
        /**
         * 重擊
         */
        CRITICAL,
        /**
         * 爆頭
         */
        HEADSHOT
    }
}

package com.ericlam.mc.minigames.core.event.player;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.bukkit.event.entity.EntityEvent;

import javax.annotation.Nullable;

/**
 * 因 CrackShot 而死亡的遊戲玩家事件
 */
public final class WeaponMechanicsDeathEvent extends GamePlayerDeathEvent {

    private final EntityEvent weaponKillEntityEvent;

    public WeaponMechanicsDeathEvent(
            @Nullable GamePlayer killer,
            GamePlayer gamePlayer,
            InGameState state,
            EntityEvent weaponKillEntityEvent,
            boolean melee
    ) {
        super(killer, gamePlayer, DeathCause.WEAPON_MECHANICS, state,
                MinigamesCore
                        .getProperties()
                        .getMessageGetter()
                        .getPure("death-msg.action.".concat(melee ? "normal" : "gun")));
        this.weaponKillEntityEvent = weaponKillEntityEvent;
    }


    /**
     * 獲取 WeaponKillEntityEvent (需要自己轉換)
     * @return WeaponKillEntityEvent
     */
    public EntityEvent getWeaponKillEntityEvent() {
        return weaponKillEntityEvent;
    }
}

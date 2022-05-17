package com.ericlam.mc.minigames.core.listeners;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.event.player.WeaponMechanicsDeathEvent;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.GameManager;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import me.deecaad.core.file.Configuration;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponKillEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class WeaponMechanicListener implements Listener {

    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final MinigamesCore api;

    public WeaponMechanicListener(MinigamesCore api) {
        this.gameManager = api.getGameManager();
        this.playerManager = api.getPlayerManager();
        this.api = api;
    }

    static boolean isWeaponMechanic(Player killer) {
        var mainHand = WeaponMechanics.getPlayerWrapper(killer).getMainHandData();
        return mainHand.getCurrentWeaponTitle() != null;
    }


    @EventHandler(ignoreCancelled = true)
    public void onWeaponKillEntity(WeaponKillEntityEvent e){
        if (!(e.getVictim() instanceof Player victim)) return;
        // 之後如果AI要加入戰場，就要改這段
        if(!(e.getShooter() instanceof Player player)) return;
        Optional<GamePlayer> gamePlayer = playerManager.findPlayer(player);
        Optional<GamePlayer> gameVictim = playerManager.findPlayer(victim);
        if (gamePlayer.isEmpty() || gameVictim.isEmpty()) return;
        boolean melee = isMelee(e.getWeaponTitle());
        var event = new WeaponMechanicsDeathEvent(gamePlayer.get(), gameVictim.get(), gameManager.getInGameState(), e, melee);
        api.getServer().getPluginManager().callEvent(event);
    }


    // TODO better checking
    static boolean isMelee(String weaponTitle){
        Configuration config = WeaponMechanics.getConfigurations();
        if (!config.getBool(weaponTitle + ".Melee.Enable_Melee")) {
            weaponTitle = config.getString(weaponTitle + ".Melee.Melee_Attachment");
            return weaponTitle == null;
        }
        return false;
    }
}

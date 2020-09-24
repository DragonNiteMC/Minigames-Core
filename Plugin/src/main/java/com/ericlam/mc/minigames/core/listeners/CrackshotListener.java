package com.ericlam.mc.minigames.core.listeners;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.event.player.CrackShotDeathEvent;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.GameManager;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class CrackshotListener implements Listener {
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final MinigamesCore api;

    public CrackshotListener(@Nonnull MinigamesCore api) {
        this.api = api;
        this.gameManager = api.getGameManager();
        this.playerManager = api.getPlayerManager();
    }


    @EventHandler(ignoreCancelled = true)
    public void onCrackshotDamage(WeaponDamageEntityEvent e) {
        if (!(e.getVictim() instanceof Player)) return;
        Player player = e.getPlayer();
        Player victim = (Player) e.getVictim();
        Entity entity = e.getDamager();
        if (e.getDamage() < victim.getHealth()) return;
        e.setCancelled(true);
        Optional<GamePlayer> gamePlayer = playerManager.findPlayer(player);
        Optional<GamePlayer> gameVictim = playerManager.findPlayer(victim);
        if (gamePlayer.isEmpty() || gameVictim.isEmpty()) return;
        Set<CrackShotDeathEvent.DamageType> types = new HashSet<>();
        if (e.isBackstab()) {
            types.add(CrackShotDeathEvent.DamageType.BACKSTAB);
        }
        if (e.isCritical()) {
            types.add(CrackShotDeathEvent.DamageType.CRITICAL);
        }
        if (e.isHeadshot()) {
            types.add(CrackShotDeathEvent.DamageType.HEADSHOT);
        }
        api.getServer().getPluginManager().callEvent(new CrackShotDeathEvent(gamePlayer.get(), gameVictim.get(), gameManager.getInGameState(), e.getWeaponTitle(), entity, types));
    }
}

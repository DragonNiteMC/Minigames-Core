package com.ericlam.mc.minigames.core.listeners;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.event.player.CrackShotDeathEvent;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.GameManager;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import me.DeeCaaD.CrackShotPlus.API;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
    private static final CSDirector csd = CSDirector.getPlugin(CSDirector.class);
    private static final CSUtility csu = API.getCSUtility();

    public CrackshotListener(@Nonnull MinigamesCore api) {
        this.api = api;
        this.gameManager = api.getGameManager();
        this.playerManager = api.getPlayerManager();
    }

    static boolean isCrackShot(Player killer, Entity damager) {
        if (damager instanceof Projectile) {
            Projectile bullet = (Projectile) damager;
            return csu.getWeaponTitle(bullet) != null;
        } else return csd.returnParentNode(killer) != null;
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

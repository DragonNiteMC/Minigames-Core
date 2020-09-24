package com.ericlam.mc.minigames.core.factory.compass;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.TeamPlayer;
import com.ericlam.mc.minigames.core.function.CircularIterator;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CompassTrackerRunnable extends BukkitRunnable {

    private final Map<GameTeam, GameTeam> targetMap;
    private final int trackerRange;
    private final CircularIterator<String> iteratorSearchText;
    private final String caughtText;
    private final Map<GamePlayer, GameTeam> individualTracker = new ConcurrentHashMap<>();
    private final GameTeam globalTeam = MinigamesCore.getPlugin(MinigamesCore.class).getGlobalTeam();

    CompassTrackerRunnable(Map<GameTeam, GameTeam> targetMap, int trackerRange, CircularIterator<String> iteratorSearchText, String caughtText) {
        this.targetMap = targetMap;
        this.trackerRange = trackerRange;
        this.iteratorSearchText = iteratorSearchText;
        this.caughtText = caughtText;
    }

    void setPlayerTracker(GamePlayer player, GameTeam target) {
        this.individualTracker.put(player, target);
    }

    @Override
    public void run() {
        PlayerManager playerManager = MinigamesCore.getApi().getPlayerManager();
        playerManager.getGamePlayer().forEach(gamer -> {
            Player player = gamer.getPlayer();
            ItemStack compass = player.getInventory().getItemInMainHand();
            if (compass.getType() != Material.COMPASS) return;
            GameTeam targetTeam;
            if (individualTracker.containsKey(gamer)) {
                targetTeam = individualTracker.get(gamer);
            } else if (gamer instanceof TeamPlayer) {
                TeamPlayer teamPlayer = gamer.castTo(TeamPlayer.class);
                targetTeam = Optional.ofNullable(targetMap.get(teamPlayer.getTeam())).orElse(globalTeam);
            } else {
                targetTeam = globalTeam;
            }
            Player target = foundNearestPlayer(gamer, targetTeam);
            String title;
            if (target == null) {
                title = iteratorSearchText.next();
            } else {
                title = caughtText
                        .replace("<target>", target.getDisplayName())
                        .replace("<distance>", Math.rint(player.getLocation().distance(target.getLocation())) + "")
                        .replace("<team>", targetTeam == globalTeam ? "§f沒有隊伍" : targetTeam.getTeamName());
                player.setCompassTarget(target.getLocation());
            }
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName(title);
            compass.setItemMeta(meta);
        });
    }

    private Player foundNearestPlayer(GamePlayer founder, GameTeam targetTeam) {
        Player player = founder.getPlayer();
        List<Entity> entities = player.getNearbyEntities(trackerRange, trackerRange, trackerRange);
        Player nearestTarget = null;
        double nearestDisance = Double.MAX_VALUE;
        GameTeam founderTeam = globalTeam;
        if (founder instanceof TeamPlayer) {
            founderTeam = founder.castTo(TeamPlayer.class).getTeam();
        }
        for (Entity entity : entities) {
            if (!(entity instanceof Player)) continue;
            Player target = (Player) entity;
            Optional<GamePlayer> targetPlayer = MinigamesCore.getApi().getPlayerManager().findPlayer(target);
            if (targetPlayer.isEmpty()) continue;
            GamePlayer targetP = targetPlayer.get();
            if (targetP.getStatus() != GamePlayer.Status.GAMING) continue;
            GameTeam targeterTeam = globalTeam;
            if (targetP instanceof TeamPlayer) {
                targeterTeam = targetP.castTo(TeamPlayer.class).getTeam();
            }
            if (founderTeam == targeterTeam && founderTeam != globalTeam) {
                continue;
            }
            if (targeterTeam != targetTeam) {
                continue;
            }
            double distance = player.getLocation().distance(target.getLocation());
            if (distance < nearestDisance) {
                nearestTarget = target;
                nearestDisance = distance;
            }
        }
        return nearestTarget;
    }
}

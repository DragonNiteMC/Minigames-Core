package com.ericlam.mc.minigames.core;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.CoreScheduler;
import com.dragonite.mc.dnmc.core.managers.WorldManager;
import com.dragonite.mc.dnmc.core.misc.world.WorldNonExistException;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public final class GameRestartRunnable extends BukkitRunnable {

    private final Arena finalArena;
    private final ArenaConfig arenaConfig;
    private final WorldManager worldManager;
    private final CoreScheduler coreScheduler;
    private long timeout;

    public GameRestartRunnable(Arena finalArena, ArenaConfig arenaConfig, long timeout) {
        var api = DragoniteMC.getAPI();
        this.finalArena = finalArena;
        this.arenaConfig = arenaConfig;
        this.timeout = timeout * 20;
        this.worldManager = api.getWorldManager();
        this.coreScheduler = api.getCoreScheduler();
        api.getBungeeManager().sendAllPlayers(arenaConfig.getFallBackServer());
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() <= 0 || timeout <= 0) {
            try {
                Bukkit.getOnlinePlayers().forEach(p -> p.teleport(arenaConfig.getLobbyLocation()));
                worldManager.unloadWorld(finalArena.getWorld().getName());
            } catch (WorldNonExistException ignored) {
            }
            coreScheduler.runTaskLater(() -> Bukkit.spigot().restart(), 20L);
        } else {
            --timeout;
        }
    }
}

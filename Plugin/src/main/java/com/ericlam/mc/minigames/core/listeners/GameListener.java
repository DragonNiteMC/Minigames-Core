package com.ericlam.mc.minigames.core.listeners;

import co.aikar.timings.Timings;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.TeamPlayer;
import com.ericlam.mc.minigames.core.config.LangConfig;
import com.ericlam.mc.minigames.core.config.MGConfig;
import com.ericlam.mc.minigames.core.event.arena.FinalArenaLoadedEvent;
import com.ericlam.mc.minigames.core.event.player.GamePlayerDeathEvent;
import com.ericlam.mc.minigames.core.event.player.GamePlayerJoinEvent;
import com.ericlam.mc.minigames.core.event.player.GamePlayerPreJoinEvent;
import com.ericlam.mc.minigames.core.event.player.GamePlayerQuitEvent;
import com.ericlam.mc.minigames.core.event.section.GamePreEndEvent;
import com.ericlam.mc.minigames.core.event.section.GamePreStartEvent;
import com.ericlam.mc.minigames.core.event.section.GameStartEvent;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.implement.TimingsSender;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.*;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSUtility;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.Arrays;

public final class GameListener implements Listener {


    private final GameManager gameManager;
    private final ScheduleManager scheduleManager;
    private final ArenaManager arenaManager;
    private final LobbyManager lobbyManager;
    private final PlayerManager playerManager;
    private final GameItemManager gameItemManager;
    private final GameStatsManager gameStatsManager;
    private final CoreInventoryManager coreInventoryManager;
    private final MinigamesCore api;
    private final MGConfig config;
    private final LangConfig msg;
    private final TimingsSender timingsSender = new TimingsSender();

    public GameListener(@Nonnull MinigamesCore api) {
        this.api = api;
        this.gameManager = api.getGameManager();
        this.scheduleManager = api.getScheduleManager();
        this.arenaManager = api.getArenaManager();
        YamlManager configManager = MinigamesCore.getConfigManager();
        this.config = configManager.getConfigAs(MGConfig.class);
        this.msg = configManager.getConfigAs(LangConfig.class);
        this.coreInventoryManager = api.getCoreInventoryManager();
        this.lobbyManager = api.getLobbyManager();
        this.gameItemManager = api.getGameItemManager();
        this.playerManager = api.getPlayerManager();
        this.gameStatsManager = api.getGameStatsManager();
    }

    @EventHandler
    public void onLeftHandSwap(PlayerSwapHandItemsEvent e) {
        if (gameManager.getGameState() == GameState.VOTING) e.setCancelled(true);
    }


    @EventHandler
    public void onSpectatorChat(AsyncPlayerChatEvent e) {
        playerManager.findPlayer(e.getPlayer()).ifPresent(g -> {
            if (g.getStatus() != GamePlayer.Status.SPECTATING) return;
            GameTeam team = g instanceof TeamPlayer ? ((TeamPlayer) g).getTeam() : null;
            e.getRecipients().removeIf(p -> playerManager.findPlayer(p).map(rec -> {
                GameTeam recTeam = rec instanceof TeamPlayer ? ((TeamPlayer) rec).getTeam() : null;
                boolean sameTeam = recTeam == team && recTeam != null;
                boolean gaming = rec.getStatus() == GamePlayer.Status.GAMING;
                return !sameTeam || gaming;
            }).orElse(false));
            e.setFormat(("§9觀戰" + (team != null ? "§7[" + team.getColor() + team.getTeamName() + "§7]" : "") + "§8//§r") + e.getFormat());
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final GameState gameState = gameManager.getGameState();
        final InGameState inGameState = gameManager.getInGameState();
        GamePlayerPreJoinEvent preJoinEvent = new GamePlayerPreJoinEvent(e.getPlayer(), gameState, inGameState);
        api.getServer().getPluginManager().callEvent(preJoinEvent);
        if (preJoinEvent.isCancelled()) return;
        final GamePlayer gamePlayer = playerManager.buildGamePlayer(e.getPlayer());
        if (gameManager.getGameState() == GameState.VOTING) {
            gameStatsManager.loadGameStats(gamePlayer).whenComplete((yes, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                if (yes) api.getLogger().info(e.getPlayer().getName() + "'s game play data loaded.");
            });
            Bukkit.broadcastMessage(api.getGamePrefix() + msg.getPure("player-join").replace("<player>", e.getPlayer().getDisplayName()));
            playerManager.setWaitingPlayer(gamePlayer);
            ((CoreLobbyManager) lobbyManager).getVoteBoard().addPlayer(gamePlayer);
            lobbyManager.tpLobbySpawn(e.getPlayer());
            if (playerManager.shouldStart()) {
                scheduleManager.startFirst(false);
            }
        }
        GamePlayerJoinEvent event = new GamePlayerJoinEvent(gamePlayer, inGameState, gameState); //gameState 是 null
        api.getServer().getPluginManager().callEvent(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        playerManager.findPlayer(player).ifPresent(g -> {
            switch (g.getStatus()) {
                case GAMING:
                    break;
                case SPECTATING:
                case WAITING:
                    e.setCancelled(true);
                    break;
            }
        });
        if (e.getClickedInventory() != null) {
            checkInventory(e.getClickedInventory(), e);
        }
    }

    @EventHandler
    public void onDragItem(InventoryDragEvent e) {
        checkInventory(e.getInventory(), e);
    }

    private void checkInventory(@Nonnull Inventory inventory, Cancellable cancellable) {
        MGConfig.InteractInventory inv = config.interactInventory;
        if (inv.list.contains(inventory.getType()) == !inv.whitelist) {
            cancellable.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        playerManager.findPlayer(player).ifPresent(g -> {
            switch (g.getStatus()) {
                case GAMING:
                    break;
                case SPECTATING:
                case WAITING:
                    e.setCancelled(true);
                    break;
            }
        });
        if (gameManager.getGameState() != GameState.IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGamePreStart(GamePreStartEvent e) {
        coreInventoryManager.loadSpectatorInventory(e.getGamingPlayer());
        ((CoreGameItemManager) gameItemManager).loadSpectatorItem(coreInventoryManager);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        playerManager.findPlayer(e.getPlayer()).ifPresent(g -> {
            api.getServer().getPluginManager().callEvent(new GamePlayerQuitEvent(g, gameManager.getInGameState(), gameManager.getGameState()));
            playerManager.removePlayer(g);
            if (gameManager.getGameState() == GameState.VOTING)
                ((CoreLobbyManager) lobbyManager).getVoteBoard().removePlayer(g);
        });
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player victim = (Player) e.getEntity();
        LivingEntity entity;
        String action;
        if (e.getDamager() instanceof Projectile) {
            entity = (LivingEntity) ((Projectile) e.getDamager()).getShooter();
            action = msg.getPure("death-msg.action.arrow");
        } else if (e.getDamager() instanceof TNTPrimed) {
            entity = (LivingEntity) ((TNTPrimed) e.getDamager()).getSource();
            action = msg.getPure("death-msg.action.tntprimed");
        } else if (e.getDamager() instanceof ThrownPotion) {
            entity = (LivingEntity) ((ThrownPotion) e.getDamager()).getShooter();
            action = msg.getPure("death-msg.action.potion");
        } else if (e.getDamager() instanceof Player) {
            entity = (LivingEntity) e.getDamager();
            action = msg.getPure("death-msg.action.normal");
        } else {
            return;
        }
        if (!(entity instanceof Player)) return;
        Player killer = (Player) entity;
        if (e.getDamager() instanceof Projectile) {
            Projectile bullet = (Projectile) e.getDamager();
            if (new CSUtility().getWeaponTitle(bullet) != null) {
                return;
            }
        } else if (CSDirector.getPlugin(CSDirector.class).returnParentNode(killer) != null) {
            return;
        }
        if (e.getFinalDamage() < victim.getHealth()) return;
        e.setCancelled(true);
        GamePlayer gameKiller = playerManager.findPlayer(killer).orElse(null);
        playerManager.findPlayer(victim).ifPresent(v -> api.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(gameKiller, v, GamePlayerDeathEvent.DeathCause.BUKKIT_KILL, gameManager.getInGameState(), action)));
    }


    @EventHandler(ignoreCancelled = true)
    public void onNormalDamage(EntityDamageEvent e) {
        switch (gameManager.getGameState()) {
            case VOTING:
            case ENDED:
            case PREEND:
            case PRESTART:
                e.setCancelled(true);
            case STOPPED:
                return;
            default:
                break;
        }
        if (e instanceof EntityDamageByEntityEvent) return;
        if (e.getEntityType() != EntityType.PLAYER) return;
        Player victim = (Player) e.getEntity();
        double damage = e.getFinalDamage();
        switch (e.getCause()) {
            case FALL:
            case VOID:
            case BLOCK_EXPLOSION:
            case LAVA:
            case FIRE:
            case FIRE_TICK:
            case POISON:
            case SUICIDE:
            case HOT_FLOOR:
            case MELTING:
            case MAGIC:
            case DROWNING:
            case FALLING_BLOCK:
            case LIGHTNING:
            case DRYOUT:
            case SUFFOCATION:
            case THORNS:
            case STARVATION:
                if (!config.damageMultiplier.disable) {
                    AttributeInstance health = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    if (health == null) break;
                    final double max = config.damageMultiplier.maxDamage;
                    final double value = Math.min(health.getBaseValue(), max);
                    damage *= (value / 20);
                    e.setDamage(damage);
                }
                break;
            default:
                break;
        }
        if (damage < victim.getHealth()) return;
        e.setCancelled(true);
        String deathMSG;
        switch (e.getCause()) {
            case FALL:
                deathMSG = msg.getPure("death-msg.fall");
                break;
            case VOID:
                deathMSG = msg.getPure("death-msg.void");
                break;
            case BLOCK_EXPLOSION:
                deathMSG = msg.getPure("death-msg.block-explode");
                break;
            case FIRE_TICK:
            case FIRE:
            case HOT_FLOOR:
            case LAVA:
                deathMSG = msg.getPure("death-msg.fire");
                break;
            case POISON:
                deathMSG = msg.getPure("death-msg.potion");
                break;
            case SUICIDE:
                deathMSG = msg.getPure("death-msg.suicide");
                break;
            case ENTITY_ATTACK:
            case ENTITY_SWEEP_ATTACK:
            case PROJECTILE:
            case ENTITY_EXPLOSION:
                return;
            case MELTING:
                deathMSG = msg.getPure("death-msg.melting");
                break;
            case MAGIC:
            case CUSTOM:
                deathMSG = msg.getPure("death-msg.magic");
                break;
            case DRYOUT:
                deathMSG = msg.getPure("death-msg.dryout");
                break;
            case DROWNING:
                deathMSG = msg.getPure("death-msg.drawn");
                break;
            case SUFFOCATION:
            case FALLING_BLOCK:
                deathMSG = msg.getPure("death-msg.no-oxygen");
                break;
            case LIGHTNING:
                deathMSG = msg.getPure("death-msg.lightning");
                break;
            case THORNS:
                deathMSG = msg.getPure("death-msg.thorns");
                break;
            case STARVATION:
                deathMSG = msg.getPure("death-msg.hunger");
                break;
            default:
                deathMSG = msg.getPure("death-msg.unknown");
                break;
        }
        playerManager.findPlayer(victim).ifPresent(v -> api.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(null, v, GamePlayerDeathEvent.DeathCause.BUKKIT_DAMAGE, gameManager.getInGameState(), deathMSG)));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setCancelled(true);
        Player player = e.getEntity();
        Player killer = player.getKiller();
        GamePlayer gamePlayer = killer == null ? null : playerManager.findPlayer(killer).orElse(null);
        /*if (ProtocolLibrary.getProtocolManager().getMinecraftVersion().getVersion().startsWith("1.14") && killer != null) {
            if (CSDirector.getPlugin(CSDirector.class).returnParentNode(killer) != null) return;
        }*/
        playerManager.findPlayer(player).ifPresent(g -> api.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(gamePlayer, g, GamePlayerDeathEvent.DeathCause.BUKKIT_DEATH, gameManager.getInGameState(), msg.getPure("death-msg.action.normal"))));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPing(PaperServerListPingEvent e) {
        e.setHidePlayers(true);
        GameState gameState = gameManager.getGameState();
        InGameState inGameState = gameManager.getInGameState();
        String motd = null;

        if (inGameState != null && inGameState.getMotd() != null) {
            motd = inGameState.getMotd();
        } else if (gameState != null) {
            motd = gameState.getMotd();
        }

        if (motd != null) e.setMotd(motd);
    }

    @EventHandler
    public void onGamePlayerDeath(GamePlayerDeathEvent e) {
        GamePlayer killer = e.getKiller();
        GamePlayer victim = e.getGamePlayer();
        if (!config.gameStatsControl.get("disable-deaths")) gameStatsManager.addDeaths(victim, 1);
        if (config.gameStatsControl.get("disable-kills")) return;
        if (killer != null) {
            if (killer instanceof TeamPlayer && victim instanceof TeamPlayer) {
                GameTeam killerTeam = killer.castTo(TeamPlayer.class).getTeam();
                GameTeam vicitmTeam = victim.castTo(TeamPlayer.class).getTeam();
                GameTeam globalteam = api.getGlobalTeam();
                if (killerTeam == vicitmTeam && killerTeam != globalteam) {
                    gameStatsManager.minusKills(killer, 1);
                    return;
                }
            }
            gameStatsManager.addKills(killer, 1);
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        if (!config.gameStatsControl.get("disable-played"))
            e.getGamingPlayer().forEach(p -> gameStatsManager.addPlayed(p, 1));
    }

    @EventHandler
    public void onFinalLoaded(FinalArenaLoadedEvent e) {
        e.getFinalArena().getWorld().getEntitiesByClasses(Item.class, Projectile.class).forEach(Entity::remove);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGameEnd(GamePreEndEvent e) {
        if (!config.gameStatsControl.get("disable-wins")) e.getWinners().forEach(g -> gameStatsManager.addWins(g, 1));
        if (!config.gameStatsControl.get("disable-save")) {
            gameStatsManager.saveAll().whenComplete((v, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                api.getLogger().info("All GameStats data has been saved.");
            });
        }
        World world = arenaManager.getFinalArena().getWorld();
        world.getEntitiesByClasses(Item.class, Projectile.class).forEach(Entity::remove);
        if (!Timings.isVerboseTimingsEnabled()) return;
        Timings.generateReport(timingsSender);
    }


    @EventHandler
    public void onClearEntities(ChunkUnloadEvent e) {
        if (MinigamesCore.getApi().getGameManager().getGameState() != GameState.ENDED) return;
        Arrays.stream(e.getChunk().getEntities()).filter(en ->
                en instanceof Item || en instanceof Projectile
        ).forEach(Entity::remove);
    }

}

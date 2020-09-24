package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.GamePlayerHandler;
import com.ericlam.mc.minigames.core.config.LangConfig;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class CorePlayerManager implements PlayerManager {

    private final Set<GamePlayer> gamePlayers = new HashSet<>();
    @Inject
    private GamePlayerHandler gamePlayerHandler;
    @Inject
    private GameItemManager gameItemManager;
    @Inject
    private LobbyManager lobbyManager;
    @Inject
    private InventoryManager inventoryManager;

    @Override
    public ImmutableList<GamePlayer> getTotalPlayers() {
        return ImmutableList.copyOf(gamePlayers);
    }

    @Override
    public ImmutableList<GamePlayer> getSpectators() {
        return ImmutableList.copyOf(gamePlayers.stream().filter(p -> p.getStatus() == GamePlayer.Status.SPECTATING).collect(Collectors.toList()));
    }

    @Override
    public ImmutableList<GamePlayer> getGamePlayer() {
        return ImmutableList.copyOf(gamePlayers.stream().filter(p -> p.getStatus() == GamePlayer.Status.GAMING).collect(Collectors.toList()));
    }

    @Override
    public void setGamePlayer(GamePlayer player) {
        player.setStatus(GamePlayer.Status.GAMING);
        player.getPlayer().getInventory().clear();
        gameItemManager.giveGameItem(player);
        gamePlayerHandler.onPlayerStatusChange(player, GamePlayer.Status.GAMING);
    }

    @Override
    public ImmutableList<GamePlayer> getWaitingPlayer() {
        return ImmutableList.copyOf(gamePlayers.stream().filter(p -> p.getStatus() == GamePlayer.Status.WAITING).collect(Collectors.toList()));
    }

    @Override
    public void setWaitingPlayer(GamePlayer player) {
        player.setStatus(GamePlayer.Status.WAITING);
        player.getPlayer().getInventory().clear();
        ((CoreGameItemManager) gameItemManager).giveJoinItem(player);
        gamePlayerHandler.onPlayerStatusChange(player, GamePlayer.Status.WAITING);
    }

    @Override
    public Optional<GamePlayer> findPlayer(Player player) {
        return gamePlayers.stream().filter(p -> p.getPlayer().equals(player)).findAny();
    }

    @Override
    public GamePlayer buildGamePlayer(Player player) {
        GamePlayer gamePlayer = gamePlayerHandler.createGamePlayer(player);
        if (this.gamePlayers.stream().noneMatch(g -> g.getPlayer() == gamePlayer.getPlayer())) {
            this.gamePlayers.add(gamePlayer);
        }
        return gamePlayer;
    }

    @Override
    public void setSpectator(GamePlayer player) {
        player.setStatus(GamePlayer.Status.SPECTATING);
        player.getPlayer().getInventory().clear();
        ((CoreInventoryManager) inventoryManager).removeGamingPlayer(player);
        ((CoreGameItemManager) gameItemManager).giveSpectatorItem(player);
        gamePlayerHandler.onPlayerStatusChange(player, GamePlayer.Status.SPECTATING);
    }

    @Override
    public boolean shouldStart() {
        boolean should = this.getWaitingPlayer().size() >= gamePlayerHandler.requireStart();
        if (!should) {
            int remain = gamePlayerHandler.requireStart() - this.getWaitingPlayer().size();
            String need = MinigamesCore.getConfigManager().getConfigAs(LangConfig.class).getPure("need-players").replace("<remain>", remain + "");
            Bukkit.broadcastMessage(MinigamesCore.getPlugin(MinigamesCore.class).getGamePrefix() + need);
        }
        return should;
    }

    @Override
    public void removePlayer(GamePlayer player) {
        if (player.getStatus() == GamePlayer.Status.WAITING) lobbyManager.unVote(player);
        gamePlayers.remove(player);
        gamePlayerHandler.onPlayerRemove(player);
    }
}

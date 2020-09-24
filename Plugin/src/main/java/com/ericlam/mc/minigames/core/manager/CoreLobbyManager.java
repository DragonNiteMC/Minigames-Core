package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.event.arena.FinalArenaLoadedEvent;
import com.ericlam.mc.minigames.core.event.player.GamePlayerUnVoteEvent;
import com.ericlam.mc.minigames.core.event.player.GamePlayerVoteEvent;
import com.ericlam.mc.minigames.core.exception.AlreadyVotedException;
import com.ericlam.mc.minigames.core.exception.arena.FinalArenaAlreadyExistException;
import com.ericlam.mc.minigames.core.exception.arena.NoFinalArenaException;
import com.ericlam.mc.minigames.core.exception.arena.UnknownArenaException;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.factory.scoreboard.ScoreboardFactory;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import java.util.*;

public final class CoreLobbyManager implements LobbyManager {

    private final Location lobby;
    private final Map<Arena, List<GamePlayer>> mapVoting = new HashMap<>();

    @Inject
    private ArenaManager arenaManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerManager playerManager;

    @Inject
    private Plugin plugin;

    private GameBoard voteBoard;

    @Inject
    public CoreLobbyManager(ArenaConfig arenaConfig) {
        lobby = arenaConfig.getLobbyLocation();
    }

    @Override
    public void tpLobbySpawn(Player player) {
        Validate.notNull(lobby, "Lobby location is null");
        player.teleportAsync(lobby);
    }

    @Override
    public ImmutableList<Arena> getCandidate() {
        return ImmutableList.copyOf(this.mapVoting.keySet());
    }

    @Override
    public Optional<Arena> getVoted(GamePlayer player) {
        for (Arena arena : mapVoting.keySet()) {
            if (this.mapVoting.get(arena).contains(player)) return Optional.of(arena);
        }
        return Optional.empty();
    }

    @Override
    public void vote(GamePlayer player, Arena arena) throws AlreadyVotedException {
        if (!mapVoting.containsKey(arena)) throw new UnknownArenaException(arena);
        GamePlayerVoteEvent voteEvent = new GamePlayerVoteEvent(arena, player);
        plugin.getServer().getPluginManager().callEvent(voteEvent);
        if (voteEvent.isCancelled()) return;
        Optional<Arena> voted = getVoted(player);
        if (voted.isPresent()) {
            Arena votedArena = voted.get();
            if (votedArena.equals(arena)) throw new AlreadyVotedException(arena);
            else unVote(player);
        }
        mapVoting.get(arena).add(player);
        voteBoard.setScore(arena.getArenaName(), mapVoting.get(arena).size());
    }

    @Override
    public void unVote(GamePlayer player) {
        if (arenaManager.getFinalArena() != null) return;
        Optional<Arena> voted = getVoted(player);
        plugin.getServer().getPluginManager().callEvent(new GamePlayerUnVoteEvent(voted.orElse(null), player));
        if (voted.isEmpty()) return;
        Arena before = voted.get();
        mapVoting.get(before).remove(player);
        voteBoard.setScore(before.getArenaName(), mapVoting.get(before).size());
    }

    @Override
    public ImmutableMap<Arena, ImmutableList<GamePlayer>> getResult() {
        Map<Arena, ImmutableList<GamePlayer>> map = new HashMap<>();
        this.mapVoting.forEach((k, v) -> map.put(k, ImmutableList.copyOf(v)));
        return ImmutableMap.copyOf(map);
    }

    @Override
    public void runFinalResult() throws NoFinalArenaException {
        Arena finalArena = null;
        int currentSize = 0;
        for (Arena arena : mapVoting.keySet()) {
            int max = mapVoting.get(arena).size();

            if (finalArena == null || Math.max(currentSize, max) == max) {
                finalArena = arena;
                currentSize = max;
            }

        }
        if (finalArena == null) throw new NoFinalArenaException();
        ((CoreArenaManager) arenaManager).setFinalArena(finalArena);
        plugin.getServer().getPluginManager().callEvent(new FinalArenaLoadedEvent(finalArena));
        voteBoard.destroy();
        playerManager.getTotalPlayers().forEach(p -> {
            p.getPlayer().closeInventory();
            p.getPlayer().getInventory().clear();
        });
    }

    void initialize(List<Arena> arenas) throws FinalArenaAlreadyExistException {
        if (gameManager.getGameState() == GameState.STOPPED) return;
        if (this.arenaManager.getFinalArena() != null) throw new FinalArenaAlreadyExistException();
        ScoreboardFactory factory = MinigamesCore.getProperties().getGameFactory().getScoreboardFactory();
        factory.setTitle("&a投票地圖");
        plugin.getLogger().info("initializing " + arenas.size() + " vote maps");
        for (Arena arena : arenas) {
            this.mapVoting.put(arena, new LinkedList<>());
            factory.setLine(arena.getArenaName(), arena.getDisplayName(), 0);
        }
        gameManager.setState(GameState.VOTING);
        factory.addSetting(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        this.voteBoard = factory.build();
    }

    public GameBoard getVoteBoard() {
        return voteBoard;
    }


}

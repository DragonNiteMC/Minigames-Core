package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.exception.gamestats.PlayerNotExistException;
import com.ericlam.mc.minigames.core.gamestats.GameStats;
import com.ericlam.mc.minigames.core.gamestats.GameStatsEditor;
import com.ericlam.mc.minigames.core.gamestats.GameStatsHandler;
import com.google.inject.Inject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class CoreGameStatsManager implements GameStatsManager {

    private final Map<OfflinePlayer, GameStatsEditor> gameStatsEditorMap = new ConcurrentHashMap<>();
    private final Map<OfflinePlayer, GameStats> originalStatsMap = new ConcurrentHashMap<>();
    private final Map<OfflinePlayer, Timestamp> playedTimeStamps = new ConcurrentHashMap<>();

    @Inject
    private GameStatsHandler gameStatsHandler;
    @Inject
    private Plugin plugin;

    private GameStatsEditor validate(OfflinePlayer player) throws PlayerNotExistException {
        return Optional.ofNullable(gameStatsEditorMap.get(player)).orElseThrow(() -> new PlayerNotExistException(player));
    }

    @Override
    public String[] getStatsInfo(GamePlayer player) throws PlayerNotExistException {
        return this.getStatsInfo(player.getPlayer());
    }

    @Override
    public String[] getStatsInfo(Player player) throws PlayerNotExistException {
        GameStatsEditor editor = validate(player);
        return editor.getInfo();
    }

    @Override
    public void addKills(GamePlayer player, int kills) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int k = editor.getKills() + kills;
            editor.setKills(k);
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void minusKills(GamePlayer player, int kills) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int k = editor.getKills() - kills;
            editor.setKills(Math.max(k, 0));
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void addDeaths(GamePlayer player, int deaths) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int d = editor.getDeaths() + deaths;
            editor.setDeaths(d);
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void minusDeaths(GamePlayer player, int deaths) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int d = editor.getDeaths() - deaths;
            editor.setDeaths(Math.max(d, 0));
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void addWins(GamePlayer player, int wins) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int w = editor.getWins() + wins;
            editor.setWins(w);
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void minusWins(GamePlayer player, int wins) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int w = editor.getWins() - wins;
            editor.setWins(Math.max(w, 0));
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void addPlayed(GamePlayer player, int played) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int p = editor.getPlayed() + played;
            editor.setPlayed(p);
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void minusPlayed(GamePlayer player, int played) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final int p = editor.getPlayed() - played;
            editor.setPlayed(Math.min(p, 0));
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void addScores(GamePlayer player, double scores) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final double score = editor.getScores() + scores;
            editor.setScores(score);
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public void minusScores(GamePlayer player, double scores) {
        try {
            GameStatsEditor editor = validate(player.getPlayer());
            final double score = editor.getScores() - scores;
            editor.setScores(score);
        } catch (PlayerNotExistException e) {
            plugin.getLogger().warning("GamePlayer (" + e.getGamePlayer().getName() + ") Not found, skipped");
        }
    }

    @Override
    public GameStatsEditor getGameStats(GamePlayer player) throws PlayerNotExistException {
        return validate(player.getPlayer());
    }

    @Override
    public CompletableFuture<Boolean> loadGameStats(GamePlayer player) {
        if (gameStatsEditorMap.containsKey(player.getPlayer())) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> gameStatsHandler.loadGameStatsData(player.getPlayer())).thenApply(gameStatsEditor -> {
            var original = gameStatsEditor.clone();
            this.originalStatsMap.put(player.getPlayer(), original);
            this.gameStatsEditorMap.put(player.getPlayer(), gameStatsEditor);
            this.playedTimeStamps.put(player.getPlayer(), Timestamp.from(Instant.now()));
            return true;
        });
    }

    @Override
    public CompletableFuture<Void> saveAll() {

        var saveStats =  gameStatsHandler.saveGameStatsData(Map.copyOf(gameStatsEditorMap));

        var recordsMap = new HashMap<OfflinePlayer, GameStats>();

        originalStatsMap.forEach((player, original) -> {

            if (!gameStatsEditorMap.containsKey(player)) return;

            var current = gameStatsEditorMap.get(player);

            recordsMap.put(player, current.minus(original));

        });

        var saveRecords = gameStatsHandler.saveGameStatsRecord(recordsMap, playedTimeStamps);

        return CompletableFuture.allOf(saveStats, saveRecords);
    }

    @Override
    public CompletableFuture<Void> savePlayer(OfflinePlayer player) throws PlayerNotExistException {
        GameStatsEditor editor = validate(player);
        GameStats record = editor.minus(originalStatsMap.get(player));
        var saveStats =  gameStatsHandler.saveGameStatsData(player, editor);
        var saveRecords = gameStatsHandler.saveGameStatsRecord(player, record, playedTimeStamps.getOrDefault(player, Timestamp.from(Instant.now())));
        return CompletableFuture.allOf(saveStats, saveRecords);
    }
}

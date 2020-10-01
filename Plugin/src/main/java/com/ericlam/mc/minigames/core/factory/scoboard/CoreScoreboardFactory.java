package com.ericlam.mc.minigames.core.factory.scoboard;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.factory.scoreboard.ScoreboardFactory;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.function.BiFunction;

public final class CoreScoreboardFactory implements ScoreboardFactory {


    private final Scoreboard scoreboard;
    private final Map<String, GameEntry<String, Integer>> sidebarLine = new LinkedHashMap<>();
    private final Set<IndividualScore> individualSideBarLine = new HashSet<>();
    private final Map<GameTeam, Map<Team.Option, Team.OptionStatus>> optionMap = new HashMap<>();
    private String title;
    private long updateTicks = 100L;

    public CoreScoreboardFactory() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    @Override
    public ScoreboardFactory addTeamSetting(GameTeam gameTeam, Team.Option option, Team.OptionStatus status) {
        this.optionMap.putIfAbsent(gameTeam, new HashMap<>());
        this.optionMap.get(gameTeam).put(option, status);
        return this;
    }

    @Override
    public ScoreboardFactory addSetting(Team.Option option, Team.OptionStatus status) {
        GameTeam globalTeam = MinigamesCore.getPlugin(MinigamesCore.class).getGlobalTeam();
        this.optionMap.putIfAbsent(globalTeam, new HashMap<>());
        this.optionMap.get(globalTeam).put(option, status);
        return this;
    }

    @Override
    public ScoreboardFactory setTitle(String title) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        return this;
    }

    @Override
    public ScoreboardFactory setLine(String key, String text, int score) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        this.sidebarLine.put(key, new GameEntry<>(text, score));
        return this;
    }


    @Override
    public ScoreboardFactory addLine(String text, int score) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        this.sidebarLine.putIfAbsent(UUID.randomUUID().toString(), new GameEntry<>(text, score));
        return this;
    }

    @Override
    public ScoreboardFactory addLine(String text, int score, BiFunction<GamePlayer, String, String> applier) {
        if (!MinigamesCore.isPacketWrapperEnabled()) {
            throw new UnsupportedOperationException("PacketWrapper didn't install, cannot use individual score in scoreboard.");
        }
        text = ChatColor.translateAlternateColorCodes('&', text);
        this.individualSideBarLine.add(new IndividualScore(text, score, applier));
        return this;
    }

    @Override
    public ScoreboardFactory setUpdateInterval(long ticks) {
        if (!MinigamesCore.isPacketWrapperEnabled()) {
            throw new UnsupportedOperationException("PacketWrapper didn't install, cannot use individual score in scoreboard.");
        }
        this.updateTicks = Math.min(ticks, 10L);
        return this;
    }

    @Override
    public GameBoard build() {
        Objective objective = scoreboard.registerNewObjective(UUID.randomUUID().toString().substring(0, 10), "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebarLine.forEach((i, entry) -> objective.getScore(entry.getKey()).setScore(entry.getValue()));
        return new CoreGameBoard(sidebarLine, individualSideBarLine, optionMap, scoreboard, updateTicks);
    }
}

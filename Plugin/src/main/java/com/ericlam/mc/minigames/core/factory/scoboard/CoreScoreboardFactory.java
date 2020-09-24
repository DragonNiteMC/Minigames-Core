package com.ericlam.mc.minigames.core.factory.scoboard;

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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CoreScoreboardFactory implements ScoreboardFactory {


    private final Scoreboard scoreboard;
    private final Map<String, GameEntry<String, Integer>> sidebarLine = new LinkedHashMap<>();
    private final Map<GameTeam, Map<Team.Option, Team.OptionStatus>> optionMap = new HashMap<>();
    private String title;
    private int index = 0;

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
        while (sidebarLine.containsKey(index + "")) {
            index++;
        }
        text = ChatColor.translateAlternateColorCodes('&', text);
        this.sidebarLine.putIfAbsent(index + "", new GameEntry<>(text, score));
        return this;
    }

    @Override
    public GameBoard build() {
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebarLine.forEach((i, entry) -> objective.getScore(entry.getKey()).setScore(entry.getValue()));
        return new CoreGameBoard(sidebarLine, optionMap, scoreboard);
    }
}

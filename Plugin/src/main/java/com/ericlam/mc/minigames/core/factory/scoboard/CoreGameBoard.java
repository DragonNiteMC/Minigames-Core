package com.ericlam.mc.minigames.core.factory.scoboard;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.TeamPlayer;
import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class CoreGameBoard implements GameBoard {

    private final Map<String, GameEntry<String, Integer>> sidebarLine;
    private final Map<GameTeam, Map<Team.Option, Team.OptionStatus>> optionMap;
    private final Scoreboard scoreboard;
    private final GameTeam globalTeam;
    private final Objective objective;

    CoreGameBoard(Map<String, GameEntry<String, Integer>> sidebarLine, Map<GameTeam, Map<Team.Option, Team.OptionStatus>> optionMap, Scoreboard scoreboard) {
        globalTeam = MinigamesCore.getPlugin(MinigamesCore.class).getGlobalTeam();
        optionMap.putIfAbsent(globalTeam, new HashMap<>());
        optionMap.get(globalTeam).putIfAbsent(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
        optionMap.get(globalTeam).putIfAbsent(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        optionMap.get(globalTeam).putIfAbsent(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        this.sidebarLine = sidebarLine;
        this.optionMap = optionMap;
        this.scoreboard = scoreboard;
        this.objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    @Override
    public void setTitle(String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        objective.setDisplayName(title);
    }

    @Override
    public void switchTeam(TeamPlayer player) {
        GameTeam newTeam = player.getTeam();
        if (newTeam == null) newTeam = globalTeam;
        Team oldTeam = scoreboard.getEntryTeam(player.getPlayer().getName());
        if (oldTeam != null) oldTeam.removeEntry(player.getPlayer().getName());
        else return;
        Team t = getOrCreateTeam(newTeam);
        t.addEntry(player.getPlayer().getName());
    }

    @Nonnull
    private Team getOrCreateTeam(final GameTeam finalTeam) {
        return Optional.ofNullable(scoreboard.getTeam(finalTeam.getTeamName())).orElseGet(() -> {
            Team createTeam = scoreboard.registerNewTeam(finalTeam.getTeamName());
            createTeam.setCanSeeFriendlyInvisibles(finalTeam != globalTeam);
            createTeam.setColor(finalTeam.getColor());
            createTeam.setPrefix(finalTeam.getColor().toString());
            createTeam.setAllowFriendlyFire(finalTeam.isEnabledFriendlyFire());
            Optional.ofNullable(optionMap.get(finalTeam)).ifPresent(m -> m.forEach(createTeam::setOption));
            return createTeam;
        });
    }


    @Override
    public void addPlayer(GamePlayer player) {
        GameTeam team = globalTeam;
        if (player instanceof TeamPlayer) {
            team = player.castTo(TeamPlayer.class).getTeam();
            if (team == null) team = globalTeam;
        }
        Team t = getOrCreateTeam(team);
        if (t.getEntries().contains(player.getPlayer().getName())) return;
        t.addEntry(player.getPlayer().getName());
        player.getPlayer().setScoreboard(scoreboard);
    }

    @Override
    public void removePlayer(GamePlayer player) {
        GameTeam team = globalTeam;
        if (player instanceof TeamPlayer) {
            team = player.castTo(TeamPlayer.class).getTeam();
            if (team == null) team = globalTeam;
        }
        Team t = scoreboard.getTeam(team.getTeamName());
        if (t == null) return;
        t.removeEntry(player.getPlayer().getName());
        if (t.getEntries().size() == 0) t.unregister();
        player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    @Override
    public ImmutableMap<String, Integer> getSidebarLine() {
        Map<String, Integer> result = new LinkedHashMap<>();
        sidebarLine.values().forEach(e -> result.put(e.getKey(), e.getValue()));
        return ImmutableMap.copyOf(result);
    }

    @Override
    public void setScore(String key, int score) {
        String oldLine = sidebarLine.get(key).getKey();
        if (oldLine == null) return;
        scoreboard.resetScores(oldLine);
        objective.getScore(oldLine).setScore(score);
        sidebarLine.put(key, new GameEntry<>(oldLine, score));
    }

    @Override
    public void setLine(String key, String line) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        String oldLine = sidebarLine.get(key).getKey();
        int oldScore = sidebarLine.get(key).getValue();
        if (oldLine != null) scoreboard.resetScores(oldLine);
        objective.getScore(line).setScore(oldScore);
        sidebarLine.put(key, new GameEntry<>(line, oldScore));
    }

    @Override
    public void setLine(String key, String line, int score) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        String oldLine = sidebarLine.get(key).getKey();
        if (oldLine != null) scoreboard.resetScores(oldLine);
        objective.getScore(line).setScore(score);
        sidebarLine.put(key, new GameEntry<>(line, score));
    }

    @Override
    public void destroy() {
        MinigamesCore.getApi().getPlayerManager().getTotalPlayers().forEach(this::removePlayer);
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        scoreboard.getTeams().forEach(Team::unregister);
        scoreboard.getObjectives().forEach(Objective::unregister);
    }


}

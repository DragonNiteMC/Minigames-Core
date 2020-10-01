package com.ericlam.mc.minigames.core.factory.scoboard;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardScore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IndividualScorePacketListener extends PacketAdapter {

    private static final Map<String, IndividualScore> scoreMap = new ConcurrentHashMap<>();
    private final Map<OfflinePlayer, Map<String, String>> playerMap = new ConcurrentHashMap<>();

    public IndividualScorePacketListener(Plugin plugin) {
        super(plugin, ListenerPriority.MONITOR, List.of(PacketType.Play.Server.SCOREBOARD_SCORE));
    }

    static void registerScore(String text, IndividualScore score) {
        scoreMap.put(text, score);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        var gamePlayerOptional = MinigamesCore.getApi().getPlayerManager().findPlayer(event.getPlayer());
        if (gamePlayerOptional.isEmpty()) return; // not game player
        var gamePlayer = gamePlayerOptional.get();
        playerMap.putIfAbsent(event.getPlayer(), new ConcurrentHashMap<>());
        var wrapper = new WrapperPlayServerScoreboardScore(event.getPacket());
        var score = scoreMap.get(wrapper.getScoreName());
        if (score == null) return; // none of score
        if (wrapper.getAction() == EnumWrappers.ScoreboardAction.CHANGE) {
            var before = wrapper.getScoreName();
            var after = score.getParser().apply(gamePlayer, before);
            wrapper.setScoreName(ChatColor.translateAlternateColorCodes('&', after));
            playerMap.get(event.getPlayer()).put(before, after);
        } else if (wrapper.getAction() == EnumWrappers.ScoreboardAction.REMOVE) {
            var before = wrapper.getScoreName();
            var current = playerMap.get(event.getPlayer()).get(before);
            var after = current == null ? score.getParser().apply(gamePlayer, before) : current;
            wrapper.setScoreName(ChatColor.translateAlternateColorCodes('&', after));
        }
    }
}

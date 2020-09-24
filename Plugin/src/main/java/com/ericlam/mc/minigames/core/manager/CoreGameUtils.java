package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.function.CircularIterator;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.inject.Inject;
import com.hypernite.mc.hnmc.core.config.MessageGetter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public final class CoreGameUtils implements GameUtils {

    private final MessageGetter msg;

    @Inject
    private Plugin plugin;

    public CoreGameUtils() {
        this.msg = MinigamesCore.getProperties().getMessageGetter();
    }

    private long[] getTimeUnit(long sec) {
        long min = 0;
        long hour = 0;
        long day = 0;

        while (sec / 60 > 0 && sec > 0) {
            min++;
            sec -= 60;
        }
        while (min / 60 > 0 && min > 0) {
            hour++;
            min -= 60;
        }

        while (hour / 24 > 0 && hour > 0) {
            day++;
            hour -= 24;
        }

        return new long[]{day, hour, min, sec};
    }


    @Override
    public String getTimeWithUnit(long sec) {
        final String dayUnit = msg.getPure("time.day");
        final String hourUnit = msg.getPure("time.hour");
        final String minUnit = msg.getPure("time.min");
        final String secUnit = msg.getPure("time.sec");
        final String split = msg.getPure("time.split");

        final long[] units = getTimeUnit(sec);
        long day = units[0];
        long hour = units[1];
        long min = units[2];
        sec = units[3];

        StringBuilder builder = new StringBuilder();
        if (day > 0) builder.append(day).append(dayUnit);
        if (hour > 0) {
            if (day > 0) builder.append(split);
            builder.append(hour).append(hourUnit);
        }
        if (min > 0) {
            if (hour > 0) builder.append(split);
            builder.append(min).append(minUnit);
        }
        if (sec > 0) {
            if (min > 0) builder.append(split);
            builder.append(sec).append(secUnit);
        }
        return builder.toString();
    }

    private String getTimerFormat(long i) {
        return i > 0 ? i >= 10 ? i + "" : "0" + i : "00";
    }

    @Override
    public String getTimer(long sec) {
        final long[] units = getTimeUnit(sec);
        long day = units[0];
        long hour = units[1];
        long min = units[2];
        sec = units[3];
        StringBuilder builder = new StringBuilder();
        if (day > 0) builder.append(getTimerFormat(day)).append(":");
        if (hour > 0) builder.append(getTimerFormat(hour)).append(":");
        builder.append(getTimerFormat(min)).append(":");
        builder.append(getTimerFormat(sec));
        return builder.toString();
    }

    @Override
    public void playSound(Player player, String[] soundString) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf(soundString[0]), Float.parseFloat(soundString[1]), Float.parseFloat(soundString[2]));
        } catch (IllegalArgumentException e) {
            player.playSound(player.getLocation(), soundString[0], Float.parseFloat(soundString[1]), Float.parseFloat(soundString[2]));
        }
    }

    @Override
    public <T> void unLagIterate(Collection<T> collection, Consumer<T> task, long period) {
        new IteratorRunnable<>(collection.iterator(), task).runTaskTimer(plugin, 0L, period);
    }

    @Override
    public void noLagTeleport(List<GamePlayer> gamePlayers, List<Location> locations, long period) {
        new TeleportRunnable(gamePlayers, locations).runTaskTimer(plugin, 0L, period);
    }

    @Override
    public void noLagTeleport(List<GamePlayer> gamePlayers, long period, Location... locations) {
        this.noLagTeleport(gamePlayers, Arrays.asList(locations), period);
    }

    private static class TeleportRunnable extends BukkitRunnable {

        private final Iterator<GamePlayer> iterator;
        private final CircularIterator<Location> locations;

        private TeleportRunnable(List<GamePlayer> gamePlayers, List<Location> locations) {
            this.iterator = gamePlayers.iterator();
            this.locations = new CircularIterator<>(locations);
        }

        @Override
        public void run() {
            if (iterator.hasNext()) {
                iterator.next().getPlayer().teleportAsync(locations.next());
            } else {
                cancel();
            }
        }
    }

    private static class IteratorRunnable<T> extends BukkitRunnable {
        private final Iterator<T> iterator;
        private final Consumer<T> task;

        private IteratorRunnable(Iterator<T> iterator, Consumer<T> task) {
            this.iterator = iterator;
            this.task = task;
        }

        @Override
        public void run() {
            if (iterator.hasNext()) task.accept(iterator.next());
            else cancel();
        }
    }
}

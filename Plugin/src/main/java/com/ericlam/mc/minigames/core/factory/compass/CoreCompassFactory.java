package com.ericlam.mc.minigames.core.factory.compass;

import com.ericlam.mc.minigames.core.function.CircularIterator;
import com.ericlam.mc.minigames.core.game.GameTeam;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CoreCompassFactory implements CompassFactory {

    private final Map<GameTeam, GameTeam> targetMap = new HashMap<>();
    private int trackerRange;
    private CircularIterator<String> iteratorSearchText;
    private String caughtText;


    public CoreCompassFactory() {

    }

    @Override
    public CompassFactory setTeamTarget(GameTeam team, GameTeam target) {
        this.targetMap.put(team, target);
        return this;
    }

    @Override
    public CompassFactory setTrackerRange(int range) {
        this.trackerRange = range;
        return this;
    }

    @Override
    public CompassFactory setSearchingText(String... text) {
        List<String> l = Arrays.stream(text).map(t -> ChatColor.translateAlternateColorCodes('&', t)).collect(Collectors.toList());
        this.iteratorSearchText = new CircularIterator<>(l);
        return this;
    }

    /*
        <target> - targetName
        <distance> - distance between target
        <team> - teamName
     */
    @Override
    public CompassFactory setCaughtText(String caughtText) {
        this.caughtText = ChatColor.translateAlternateColorCodes('&', caughtText);
        return this;
    }


    @Override
    public CompassTracker build() {
        Validate.notNull(iteratorSearchText, "Searching Text is null");
        Validate.notNull(caughtText, "Caught Text is null");
        Validate.isTrue(trackerRange > 5, "Tracking Range should bigger than 5");
        return new CoreCompassTracker(targetMap, trackerRange, iteratorSearchText, caughtText);
    }
}

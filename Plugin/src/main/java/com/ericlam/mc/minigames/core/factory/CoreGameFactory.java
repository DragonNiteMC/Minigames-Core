package com.ericlam.mc.minigames.core.factory;

import com.ericlam.mc.minigames.core.factory.compass.CompassFactory;
import com.ericlam.mc.minigames.core.factory.compass.CoreCompassFactory;
import com.ericlam.mc.minigames.core.factory.scoboard.CoreScoreboardFactory;
import com.ericlam.mc.minigames.core.factory.scoreboard.ScoreboardFactory;

public final class CoreGameFactory implements GameFactory {


    @Override
    public ScoreboardFactory getScoreboardFactory() {
        return new CoreScoreboardFactory();
    }

    @Override
    public CompassFactory getCompassFactory() {
        return new CoreCompassFactory();
    }

}

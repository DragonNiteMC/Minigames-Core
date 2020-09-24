package com.ericlam.mc.minigames.core.factory.compass;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.function.CircularIterator;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.main.MinigamesCore;

import java.util.Map;

public final class CoreCompassTracker implements CompassTracker {

    private final CompassTrackerRunnable runnable;
    private boolean running;

    CoreCompassTracker(Map<GameTeam, GameTeam> targetMap, int trackerRange, CircularIterator<String> iteratorSearchText, String caughtText) {
        this.runnable = new CompassTrackerRunnable(targetMap, trackerRange, iteratorSearchText, caughtText);
    }

    @Override
    public void setIndividualTarget(GamePlayer player, GameTeam team) {
        runnable.setPlayerTracker(player, team);
    }

    @Override
    public void launch() {
        if (running) return;
        runnable.runTaskTimer(MinigamesCore.getPlugin(MinigamesCore.class), 0L, 20L);
        this.running = true;
    }

    @Override
    public void destroy() {
        if (!running) return;
        runnable.cancel();
    }
}

package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.SchedulerRunnable;
import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.event.state.InGameStateSwitchEvent;
import com.ericlam.mc.minigames.core.exception.task.TaskAlreadyRunningException;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.InGameState;
import com.ericlam.mc.minigames.core.injection.GameProgramTasks;
import com.google.inject.Inject;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Optional;

public final class CoreScheduleManager implements ScheduleManager {

    private final LinkedHashMap<InGameState, GameEntry<SectionTask, InGameState>> programTasks;

    @Inject
    private GameManager gameManager;

    private SchedulerRunnable currentTask;

    @Inject
    private Plugin plugin;

    private InGameState currentGameState;

    @Inject
    private PlayerManager playerManager;

    @Inject
    public CoreScheduleManager(GameProgramTasks tasks) {
        this.programTasks = tasks.getFinalTask();
    }

    @Override
    public void startFirst(boolean forceStart) {
        InGameState firstState = programTasks.keySet().stream().findFirst().orElseThrow();
        SectionTask task = this.programTasks.get(firstState).getKey();
        if (task.isRunning()) return;
        this.start(firstState, forceStart);
    }

    private void start(InGameState state, boolean forceStart) throws TaskAlreadyRunningException {
        GameEntry<SectionTask, InGameState> entry = Optional.ofNullable(this.programTasks.get(state)).orElseThrow();
        SectionTask task = entry.getKey();
        if (task.isRunning()) throw new TaskAlreadyRunningException(state);

        final Runnable nextRun = () -> {
            InGameState nextState = entry.getValue();
            if (nextState == null) return;
            if (this.isRunning(nextState)) return;
            this.start(nextState);
        };

        if (currentTask != null && !currentTask.isCancelled()) {
            return;
        }

        currentTask = new SchedulerRunnable(task, nextRun, forceStart);
        task.initTimer(playerManager);
        currentTask.runTaskTimer(plugin, 0L, 20L);
        if (currentGameState == state) return;
        this.currentGameState = state;
        plugin.getServer().getPluginManager().callEvent(new InGameStateSwitchEvent(state, gameManager.getGameState(), playerManager));
    }

    @Override
    public void start(InGameState state) throws TaskAlreadyRunningException {
        this.start(state, false);
    }

    @Override
    public boolean isRunning(InGameState state) {
        SectionTask task = Optional.ofNullable(this.programTasks.get(state).getKey()).orElseThrow();
        return task.isRunning();
    }

    @Override
    public void cancelCurrent() {
        if (currentTask == null || currentTask.isCancelled()) return;
        currentTask.cancel();
    }

    @Override
    public void finishCurrent() {
        if (currentTask == null || currentTask.isCancelled()) return;
        currentTask.finish(true);
    }

    @Override
    public void jumpInto(InGameState state, boolean cancel) {
        if (currentTask != null && !currentTask.isCancelled()) {
            if (cancel) currentTask.cancel();
            else currentTask.finish(false);
        }
        this.start(state);
    }

    @Nullable
    @Override
    public InGameState getCurrentGameState() {
        return currentGameState != null ? isRunning(currentGameState) ? currentGameState : null : null;
    }
}

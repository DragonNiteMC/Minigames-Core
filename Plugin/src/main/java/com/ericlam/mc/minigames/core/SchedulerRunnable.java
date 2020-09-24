package com.ericlam.mc.minigames.core;

import com.ericlam.mc.minigames.core.config.MGConfig;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import org.bukkit.scheduler.BukkitRunnable;

public final class SchedulerRunnable extends BukkitRunnable {

    private final Runnable nextRun;
    private final boolean forceStart;
    private final SectionTask task;
    private final MinigamesAPI api;
    private long currentTime;

    public SchedulerRunnable(final SectionTask task, final Runnable nextRun, final boolean forceStart) {
        this.api = MinigamesCore.getApi();
        this.task = task;
        currentTime = forceStart ? MinigamesCore.getConfigManager().getConfigAs(MGConfig.class).forceStartTime : task.getTotalTime();
        this.nextRun = nextRun;
        task.setRunning(true);
        this.forceStart = forceStart;
    }

    public SchedulerRunnable(final SectionTask task, final Runnable nextRun) {
        this(task, nextRun, false);
    }

    @Override
    public void run() {
        if (currentTime > 0) {
            if (task.shouldCancel() && !forceStart) {
                this.cancel();
                return;
            }
            currentTime = task.run(currentTime);
            currentTime--;
        } else {
            if (forceStart && api.getArenaManager().getFinalArena() == null) {
                api.getLobbyManager().runFinalResult();
            }
            this.finish(true);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        task.setRunning(false);
        task.onCancel();
    }


    public void finish(boolean nextStep) {
        super.cancel();
        task.setRunning(false);
        task.onFinish();
        if (nextStep) nextRun.run();
    }
}

package com.ericlam.mc.minigames.core.exception.task;

import com.ericlam.mc.minigames.core.exception.GameRunException;
import com.ericlam.mc.minigames.core.game.InGameState;

public class TaskAlreadyRunningException extends GameRunException {

    public TaskAlreadyRunningException(InGameState state) {
        super("The Task of InGameState (" + state.getStateName() + ") has already running. ", false);
    }
}

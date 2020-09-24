package com.ericlam.mc.minigames.core.injection;

import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.InGameState;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public final class GameProgramTasks {

    private final LinkedHashMap<InGameState, GameEntry<SectionTask, InGameState>> finalTask = new LinkedHashMap<>();
    private final InGameState lobbyState = new InGameState("lobby", "&b倒數中");
    private final InGameState preEndState = new InGameState("preEnd", GameState.PREEND.getMotd());

    public GameProgramTasks(@Nonnull SectionTask lobbyTask, @Nonnull SectionTask endTask, LinkedHashMap<InGameState, GameEntry<SectionTask, InGameState>> gameTasks) {
        if (gameTasks.isEmpty()) {
            finalTask.put(lobbyState, new GameEntry<>(lobbyTask, preEndState));
        } else {
            InGameState firstInGameState = new LinkedList<>(gameTasks.keySet()).getFirst();
            InGameState lastInGameState = new LinkedList<>(gameTasks.keySet()).getLast();
            finalTask.put(lobbyState, new GameEntry<>(lobbyTask, firstInGameState));
            finalTask.putAll(gameTasks);
            finalTask.get(lastInGameState).setValue(preEndState);
        }

        finalTask.put(preEndState, new GameEntry<>(endTask, null));
    }


    public LinkedHashMap<InGameState, GameEntry<SectionTask, InGameState>> getFinalTask() {
        return finalTask;
    }


    public InGameState getLobbyState() {
        return lobbyState;
    }


    public InGameState getPreEndState() {
        return preEndState;
    }
}

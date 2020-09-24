package com.ericlam.mc.minigames.core.exception;

import com.ericlam.mc.minigames.core.arena.Arena;

public class AlreadyVotedException extends Exception {

    private Arena votedArena;

    public AlreadyVotedException(Arena votedArena) {
        this.votedArena = votedArena;
    }

    public Arena getVotedArena() {
        return votedArena;
    }
}

package com.ericlam.mc.minigames.core.factory.scoboard;

import com.ericlam.mc.minigames.core.factory.scoreboard.GameBoard;
import org.bukkit.scheduler.BukkitRunnable;

final class IndividualUpdateRunnable extends BukkitRunnable {
    private final GameBoard gameBoard;

    IndividualUpdateRunnable(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void run() {
        gameBoard.updateIndividual();
    }
}

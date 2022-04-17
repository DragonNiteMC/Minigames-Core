package com.ericlam.mc.minigames.core.injection;

import com.ericlam.mc.minigames.core.function.GameEntry;
import com.dragonnite.mc.dnmc.core.managers.builder.AbstractInventoryBuilder;

import java.util.List;

public final class InventoryGui {

    private final GameEntry<AbstractInventoryBuilder, List<Integer>> voteGUI;

    public InventoryGui(GameEntry<AbstractInventoryBuilder, List<Integer>> voteGUI) {
        this.voteGUI = voteGUI;
    }

    public GameEntry<AbstractInventoryBuilder, List<Integer>> getVoteGUI() {
        return voteGUI;
    }
}

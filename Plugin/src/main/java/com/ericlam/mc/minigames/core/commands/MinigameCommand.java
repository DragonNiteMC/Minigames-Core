package com.ericlam.mc.minigames.core.commands;

import com.dragonnite.mc.dnmc.core.misc.commands.DefaultCommand;

public final class MinigameCommand extends DefaultCommand {
    public MinigameCommand() {
        super(null, "mgcore", null, "小遊戲核心專用指令", "mg-core", "mgc", "mg");
        super.addSub(new TpsRecordCommand(this));
        super.addSub(new GameStatsCommand(this));
        super.addSub(new ForceStartCommand(this));
    }
}

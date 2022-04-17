package com.ericlam.mc.minigames.core.commands;

import co.aikar.timings.Timings;
import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class TpsRecordCommand extends CommandNode {

    public static Set<CommandSender> reportTo = new HashSet<>();

    public TpsRecordCommand(CommandNode parent) {
        super(parent, "record-timings", Perm.HELPER, "記錄TPS", null, "record-lag", "record-tps");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        Optional<MinigamesAPI> apiSafe = MinigamesCore.getApiSafe();
        if (apiSafe.isEmpty()) {
            commandSender.sendMessage(DragonNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "Minigames-API 沒有被啟動。");
            return true;
        }
        if (!Timings.isVerboseTimingsEnabled()) {
            Timings.setTimingsEnabled(true);
            Timings.setVerboseTimingsEnabled(true);
            commandSender.sendMessage("§a成功啟動 Timings 記錄。");
        }
        commandSender.sendMessage("§a系統將在遊戲完結後自動生成 Timings report 予你。");
        reportTo.add(commandSender);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        return null;
    }
}

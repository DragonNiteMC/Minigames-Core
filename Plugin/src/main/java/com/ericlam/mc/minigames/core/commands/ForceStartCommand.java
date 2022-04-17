package com.ericlam.mc.minigames.core.commands;

import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public final class ForceStartCommand extends CommandNode {

    public ForceStartCommand(CommandNode parent) {
        super(parent, "forcestart", Perm.ADMIN, "強制開始遊戲", null, "start", "force-start");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        final String prefix = DragonNiteMC.getAPI().getCoreConfig().getPrefix();
        final String warnPrefix = prefix + ChatColor.RED;
        final String successPrefix = prefix + ChatColor.GREEN;
        Optional<MinigamesAPI> apiSafe = MinigamesCore.getApiSafe();
        if (apiSafe.isEmpty()) {
            commandSender.sendMessage(warnPrefix + "Minigames-API 沒有被啟動。");
            return true;
        }
        MinigamesAPI api = apiSafe.get();
        if (api.getGameManager().getInGameState() != null) {
            commandSender.sendMessage(warnPrefix + "遊戲或倒數程序已經開始。");
            return true;
        }
        api.getScheduleManager().startFirst(true);
        var plugin = MinigamesCore.getPlugin(MinigamesCore.class);
        String gamePrefix = plugin.getGamePrefix() + ChatColor.AQUA;
        commandSender.sendMessage(successPrefix + "快速開始成功。");
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(gamePrefix + "遊戲已被管理員強制開始。"));
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        return null;
    }
}

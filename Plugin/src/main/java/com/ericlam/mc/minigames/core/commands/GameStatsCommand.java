package com.ericlam.mc.minigames.core.commands;

import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.config.LangConfig;
import com.ericlam.mc.minigames.core.exception.gamestats.PlayerNotExistException;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class GameStatsCommand extends CommandNode {

    public GameStatsCommand(CommandNode parent) {
        super(parent, "game-stats", null, "查看別人/玩家戰績", "<player>", "mg-stats", "stats", "mg-info");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        CoreConfig coreConfig = HyperNiteMC.getAPI().getCoreConfig();
        Player target;
        if (list.size() < 1) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(coreConfig.getPrefix() + coreConfig.getNotPlayer());
                return true;
            }
            target = (Player) commandSender;
        } else {
            final String name = list.get(0);
            target = Bukkit.getPlayer(name);
            if (target == null) {
                commandSender.sendMessage(coreConfig.getPrefix() + coreConfig.getNotFoundPlayer());
                return true;
            }
        }

        Optional<MinigamesAPI> apiSafe = MinigamesCore.getApiSafe();
        if (apiSafe.isEmpty()) {
            commandSender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + "Minigames-API 沒有被啟動。");
            return true;
        }
        YamlManager configManager = MinigamesCore.getConfigManager();
        LangConfig lang = configManager.getConfigAs(LangConfig.class);
        MinigamesAPI api = apiSafe.get();
        String prefix = ChatColor.translateAlternateColorCodes('&', api.getGameManager().getGamePrefix());
        try {
            String[] info = api.getGameStatsManager().getStatsInfo(target);
            commandSender.sendMessage(prefix + lang.getPure("gamestats-info").replace("<player>", target.getDisplayName()));
            commandSender.sendMessage(Arrays.stream(info).map(l -> prefix + ChatColor.translateAlternateColorCodes('&', l)).toArray(String[]::new));
        } catch (PlayerNotExistException e) {
            commandSender.sendMessage(prefix + lang.getPure("gamestats-not-found"));
        }
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        return null;
    }
}

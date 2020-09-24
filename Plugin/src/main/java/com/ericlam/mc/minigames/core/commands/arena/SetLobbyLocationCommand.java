package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.ericlam.mc.minigames.core.manager.CoreArenaCreateManager;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class SetLobbyLocationCommand extends PreSetArenaCommandNode {

    public SetLobbyLocationCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "setlobby", "設置大堂重生點", null, "setspawn", "setrespawn");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        CoreArenaCreateManager createManager = (CoreArenaCreateManager) arenaCreateManager;
        var loc = player.getLocation();
        if (!Bukkit.getWorlds().get(0).getName().equals(loc.getWorld().getName())) {
            sendMessage(player, "arena.lobby-not-default");
            return true;
        }
        handleComplete(createManager.setLobbyLocation(loc), player);
        return true;
    }
}

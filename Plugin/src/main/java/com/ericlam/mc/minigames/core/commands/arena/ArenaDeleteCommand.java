package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaDeleteCommand extends PreSetArenaCommandNode {

    public ArenaDeleteCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "delete", "場地刪除指令", "<arena>", "remove");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        try {
            handleComplete(arenaCreateManager.deleteArena(arena), player);
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        }
        return true;
    }


}

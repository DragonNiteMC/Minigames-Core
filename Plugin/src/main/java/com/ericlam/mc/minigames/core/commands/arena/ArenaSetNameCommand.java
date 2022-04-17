package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNameExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaSetNameCommand extends PreSetArenaCommandNode {

    public ArenaSetNameCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "setname", "設置場地名稱", "<arena> <name>");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        final String name = list.get(1);
        try {
            arenaCreateManager.setName(arena, name);
            sendMessage(player, "arena.result.success");
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        } catch (ArenaNameExistException e) {
            sendMessage(player, "arena.name-exist", e.getArena());
        }
        return true;
    }
}

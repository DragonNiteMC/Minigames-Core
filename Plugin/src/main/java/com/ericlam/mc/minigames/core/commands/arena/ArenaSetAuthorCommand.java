package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaSetAuthorCommand extends PreSetArenaCommandNode {

    public ArenaSetAuthorCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "setauthor", "設置場地作者", "<arena> <author>");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String name = list.get(0);
        list.remove(0);
        final String author = String.join(", ", list);
        try {
            arenaCreateManager.setAuthor(name, author);
            sendMessage(player, "arena.result.success");
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        }
        return true;
    }


}

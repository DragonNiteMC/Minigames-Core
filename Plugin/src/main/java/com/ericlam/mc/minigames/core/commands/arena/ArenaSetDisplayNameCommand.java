package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonite.mc.dnmc.core.managers.YamlManager;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaSetDisplayNameCommand extends PreSetArenaCommandNode {

    public ArenaSetDisplayNameCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "setdisplay", "設置顯示名稱", "<arena> <display>");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        list.remove(0);
        final String display = String.join(" ", list);
        try {
            arenaCreateManager.setDisplayName(arena, display);
            sendMessage(player, "arena.result.success");
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        }
        return true;
    }

}

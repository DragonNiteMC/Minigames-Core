package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaAddStoryLineCommand extends PreSetArenaCommandNode {

    public ArenaAddStoryLineCommand(YamlManager minigameConfig, CommandNode parent) {
        super(minigameConfig, parent, "addline", "新增地圖描述行數", "<arena> <text>", "addstoryline", "adddescriptionline");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) throws ArenaNotExistException {
        final String arena = list.get(0);
        list.remove(0);
        final String line = ChatColor.translateAlternateColorCodes('&', String.join(" ", list));
        arenaCreateManager.addDescriptionLine(arena, line);
        sendMessage(player, "arena.result.success");
        return true;
    }
}

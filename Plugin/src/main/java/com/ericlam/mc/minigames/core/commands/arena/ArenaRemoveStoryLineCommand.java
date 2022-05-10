package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.NoMoreElementException;
import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonite.mc.dnmc.core.managers.YamlManager;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaRemoveStoryLineCommand extends PreSetArenaCommandNode {

    public ArenaRemoveStoryLineCommand(YamlManager minigameConfig, CommandNode parent) {
        super(minigameConfig, parent, "removeline", "刪除地圖描述最尾行數", "<arena>", "removestoryline", "removedescriptionline");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) throws ArenaNotExistException {
        final String arena = list.get(0);
        try {
            arenaCreateManager.removeDescriptionLine(arena);
            sendMessage(player, "arena.result.success");
        } catch (NoMoreElementException e) {
            sendMessage(player, "arena.no-more-line", e.getArena());
        }
        return true;
    }
}

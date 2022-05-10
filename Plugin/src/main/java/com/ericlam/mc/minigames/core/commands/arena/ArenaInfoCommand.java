package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonite.mc.dnmc.core.managers.YamlManager;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public final class ArenaInfoCommand extends PreSetArenaCommandNode {

    public ArenaInfoCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "info", "查看場地資訊", "<arena>", "see");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        try {
            String prefix = MinigamesCore.getPlugin(MinigamesCore.class).getGamePrefix();
            String[] info = Arrays.stream(arenaCreateManager.getArenaInfo(arena)).map(prefix::concat).toArray(String[]::new);
            sendMessage(player, "arena.info", arena);
            player.sendMessage(info);
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        }
        return true;
    }
}

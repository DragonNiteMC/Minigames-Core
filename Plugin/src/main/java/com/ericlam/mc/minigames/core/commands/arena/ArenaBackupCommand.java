package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.BackupNotAllowedException;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaBackupCommand extends PreSetArenaCommandNode {

    public ArenaBackupCommand(YamlManager minigameConfig, CommandNode parent) {
        super(minigameConfig, parent, "backup", "遊戲地圖備份", "<arena>", "copy");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) throws ArenaNotExistException {
        final String arena = list.get(0);
        try {
            sendMessage(player, "backing-up", arena);
            handleComplete(arenaCreateManager.backupArena(arena).thenApply(file -> {
                if (file != null) {
                    var path = file.getPath().replace(MinigamesCore.getPlugin(MinigamesCore.class).getDataFolder().getPath(), "");
                    var msg = getMessage("arena.backup-complete", arena).replace("<path>", path);
                    player.sendMessage(msg);
                    return true;
                }
                return false;
            }), player);
        } catch (BackupNotAllowedException e) {
            sendMessage(player, "arena.backup-not-allowed");
        }
        return true;
    }
}

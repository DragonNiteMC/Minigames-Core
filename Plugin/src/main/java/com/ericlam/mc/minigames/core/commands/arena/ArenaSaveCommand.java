package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotBackupException;
import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.ArenaUnchangedExcpetion;
import com.ericlam.mc.minigames.core.exception.arena.create.SetUpNotFinishException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaSaveCommand extends PreSetArenaCommandNode {

    public ArenaSaveCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "save", "保存場地", "<arena>");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        try {
            handleComplete(arenaCreateManager.saveArena(arena), player);
        } catch (SetUpNotFinishException e) {
            sendMessage(player, "arena.setup-not-completed", e.getArena());
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        } catch (ArenaUnchangedExcpetion e) {
            sendMessage(player, "arena.unchanged", e.getArena());
        } catch (ArenaNotBackupException e) {
            sendMessage(player, "arena.not-back-up", e.getArena());
        }
        return true;
    }
}

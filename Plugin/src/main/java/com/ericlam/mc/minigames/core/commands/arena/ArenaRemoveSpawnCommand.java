package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.NoMoreLocationException;
import com.ericlam.mc.minigames.core.exception.arena.create.WarpNotExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaRemoveSpawnCommand extends PreSetArenaCommandNode {

    public ArenaRemoveSpawnCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "delspawn", "刪除坐標", "<arena> <warp>", "removespawn");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        final String warp = list.get(1);
        try {
            arenaCreateManager.removeSpawn(arena, warp);
            sendMessage(player, "arena.result.success");
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        } catch (WarpNotExistException e) {
            sendMessage(player, "arena.warp.not-exist", e.getWarp());
        } catch (NoMoreLocationException e) {
            sendMessage(player, "arena.location.no-locations", e.getWarp());
        }
        return true;
    }
}

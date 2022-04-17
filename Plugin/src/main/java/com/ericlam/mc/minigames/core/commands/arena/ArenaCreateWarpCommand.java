package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.IllegalWarpException;
import com.ericlam.mc.minigames.core.exception.arena.create.WarpExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaCreateWarpCommand extends PreSetArenaCommandNode {

    public ArenaCreateWarpCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "createwarp", "創建坐標", "<arena> <warp>", "addwarp");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String arena = list.get(0);
        final String warp = list.get(1);
        try {
            arenaCreateManager.createWarp(arena, warp);
            sendMessage(player, "arena.result.success");
        } catch (ArenaNotExistException e) {
            sendMessage(player, "arena.not-exist", e.getArena());
        } catch (WarpExistException e) {
            sendMessage(player, "arena.warp.exist", e.getWarp());
        } catch (IllegalWarpException e) {
            sendMessage(player, "arena.warp.illegal", e.getWarp(), e.getAllowWarps());
        }
        return true;
    }
}

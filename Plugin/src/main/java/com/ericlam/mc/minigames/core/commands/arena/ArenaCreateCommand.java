package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.config.MGConfig;
import com.ericlam.mc.minigames.core.exception.arena.create.ArenaExistException;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class ArenaCreateCommand extends PreSetArenaCommandNode {


    public ArenaCreateCommand(YamlManager configManager, CommandNode parent) {
        super(configManager, parent, "create", "創建場地", "<name>", "add");
    }

    @Override
    protected boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) {
        final String name = list.get(0);
        try {
            boolean allow = minigameConfig.getConfigAs(MGConfig.class).allowMultiArenaInOneWorld;
            if (!allow && arenaCreateManager.getArenasFromWorld(player.getWorld()).length > 0) {
                sendMessage(player, "arena.world-contain-arena");
                return true;
            }
            arenaCreateManager.createArena(name, player);
            sendMessage(player, "arena.result.success");
            return true;
        } catch (ArenaExistException e) {
            sendMessage(player, "arena.exist", e.getArena());
        }
        return true;
    }


}

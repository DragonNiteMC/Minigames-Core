package com.ericlam.mc.minigames.core.commands;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.exception.arena.create.ArenaNotExistException;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.ericlam.mc.minigames.core.manager.ArenaCreateManager;
import com.hypernite.mc.hnmc.core.config.MessageGetter;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 場地指令接口，用於新增額外場地設置指令是掛接。
 */
public abstract class ArenaCommandNode extends CommandNode {

    private final MessageGetter messageGetter;


    /**
     * @param parent      父指令
     * @param command     指令名稱
     * @param permission  權限
     * @param description 描述
     * @param placeholder 參數
     * @param alias       別稱
     */
    public ArenaCommandNode(CommandNode parent, @Nonnull String command, String permission, @Nonnull String description, String placeholder, String... alias) {
        super(parent, command, permission, description, placeholder, alias);
        messageGetter = MinigamesCore.getProperties().getMessageGetter();
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        CoreConfig coreConfig = HyperNiteMC.getAPI().getCoreConfig();
        String prefix = MinigamesCore.getApi().getGameManager().getGamePrefix();
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(coreConfig.getPrefix() + coreConfig.getNotPlayer());
            return true;
        }

        Player player = (Player) commandSender;
        final ArenaCreateManager arenaCreateManager = MinigamesCore.getApi().getArenaCreateManager();
        Validate.notNull(arenaCreateManager, "Arena Create Manager is null");

        if (MinigamesCore.getApi().getGameManager().getGameState() != GameState.STOPPED) {
            player.sendMessage(prefix + messageGetter.getPure("game-not-stopped"));
            return true;
        }

        try {
            return executeArenaOperation(player, list, arenaCreateManager);
        } catch (ArenaNotExistException e) {
            player.sendMessage(prefix + messageGetter.getPure("arena.not-exist").replace("<arena>", e.getArena()));
            return true;
        }
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        if (this.getPlaceholder() == null) return null;
        String[] placeholders = Arrays.stream(this.getPlaceholder().split(" ")).map(l -> l.replace("<", "").replace(">", "").replace("[", "").replace("]", "")).toArray(String[]::new);
        List<Integer> arenaTab = new LinkedList<>();
        List<Integer> warpTab = new LinkedList<>();
        for (int i = 0; i < placeholders.length; i++) {
            final String var = placeholders[i];
            if (var.equalsIgnoreCase("arena")) {
                arenaTab.add(i);
            } else if (var.equalsIgnoreCase("warp")) {
                warpTab.add(i);
            }
        }
        final ArenaCreateManager arenaCreateManager = MinigamesCore.getApi().getArenaCreateManager();
        Validate.notNull(arenaCreateManager, "Arena Create Manager is null");
        final int index = list.size() - 1;
        if (arenaTab.contains(index)) {
            return arenaCreateManager.getArenaList().stream().map(Arena::getArenaName).collect(Collectors.toList());
        } else if (warpTab.contains(index)) {
            final int arenaIndex = index - 1;
            if (placeholders.length > arenaIndex && placeholders[arenaIndex].equalsIgnoreCase("arena")) {
                try {
                    return arenaCreateManager.getWarpList(list.get(arenaIndex));
                } catch (ArenaNotExistException ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 指令運行
     *
     * @param player             指令發送者
     * @param list               參數
     * @param arenaCreateManager 場地設置管理器
     * @return 執行成功
     * @throws ArenaNotExistException 場地不存在
     */
    protected abstract boolean executeArenaOperation(@Nonnull Player player, @Nonnull List<String> list, @Nonnull ArenaCreateManager arenaCreateManager) throws ArenaNotExistException;


}

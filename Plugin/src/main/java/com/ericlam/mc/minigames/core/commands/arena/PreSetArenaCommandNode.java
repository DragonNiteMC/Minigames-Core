package com.ericlam.mc.minigames.core.commands.arena;

import com.ericlam.mc.minigames.core.commands.ArenaCommandNode;
import com.ericlam.mc.minigames.core.config.LangConfig;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public abstract class PreSetArenaCommandNode extends ArenaCommandNode {

    protected final YamlManager minigameConfig;
    protected final LangConfig langConfig;


    public PreSetArenaCommandNode(YamlManager minigameConfig, CommandNode parent, @Nonnull String command, @Nonnull String description, String placeholder, String... alias) {
        super(parent, command, Perm.OWNER, description, placeholder, alias);
        this.minigameConfig = minigameConfig;
        this.langConfig = minigameConfig.getConfigAs(LangConfig.class);
    }

    protected void sendMessage(Player player, String path) {
        String prefix = ((MinigamesCore) MinigamesCore.getApi()).getGamePrefix();
        player.sendMessage(prefix + langConfig.getPure(path));
    }

    protected void sendMessage(Player player, String path, final String value) {
        var msg = getMessage(path, value);
        player.sendMessage(msg);
    }

    protected void sendMessage(Player player, String path, final String value, final String[] list) {
        var msg = getMessage(path, value).replace("<list>", Arrays.toString(list));
        player.sendMessage(msg);
    }

    protected String getMessage(String path, final String value) {
        String prefix = ((MinigamesCore) MinigamesCore.getApi()).getGamePrefix();
        return prefix + langConfig.getPure(path).replace("<arena>", value).replace("<warp>", value);
    }

    protected void handleComplete(CompletableFuture<Boolean> completableFuture, Player player) {
        completableFuture.whenComplete((aBoolean, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            final String path = "arena.result.".concat(aBoolean ? "success" : "fail");

            sendMessage(player, path);
        });
    }
}

package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.config.ItemConfig;
import com.ericlam.mc.minigames.core.config.LangConfig;
import com.ericlam.mc.minigames.core.exception.AlreadyVotedException;
import com.ericlam.mc.minigames.core.exception.arena.ArenaNotLoadedException;
import com.ericlam.mc.minigames.core.exception.arena.FinalArenaAlreadyExistException;
import com.ericlam.mc.minigames.core.injection.InventoryGui;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.hypernite.mc.hnmc.core.builders.InventoryBuilder;
import com.hypernite.mc.hnmc.core.builders.ItemStackBuilder;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.hypernite.mc.hnmc.core.managers.builder.AbstractInventoryBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public final class CoreInventoryManager implements InventoryManager {

    private final Map<UUID, ItemStack> headMap = new HashMap<>();
    private final LangConfig langConfig;
    private final ItemConfig itemConfig;
    @Inject
    private InventoryGui inventoryGui;
    @Inject
    private ArenaManager arenaManager;
    @Inject
    private Plugin plugin;
    @Inject
    private PlayerManager playerManager;
    private Inventory voteInventory;
    private Inventory spectatorTpInventory;

    public CoreInventoryManager() {
        YamlManager yamlManager = MinigamesCore.getConfigManager();
        this.itemConfig = yamlManager.getConfigAs(ItemConfig.class);
        this.langConfig = yamlManager.getConfigAs(LangConfig.class);
    }

    public void loadSpectatorInventory(ImmutableList<GamePlayer> gamePlayers) {
        if (gamePlayers.stream().anyMatch(p -> p.getStatus() != GamePlayer.Status.GAMING))
            throw new IllegalStateException("at least one player is not game player.");
        ItemConfig.GameItem properties = itemConfig.headItem;
        String display = ChatColor.translateAlternateColorCodes('&', properties.name);
        List<String> lore = properties.lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList());
        int row = (int) Math.ceil(((double) gamePlayers.size() / 9));
        InventoryBuilder builder = new InventoryBuilder((row == 0 ? 1 : row), "傳送玩家");
        headMap.clear();
        for (GamePlayer gamePlayer : gamePlayers) {
            new ItemStackBuilder(Material.PLAYER_HEAD)
                    .displayName(display.replace("<name>", gamePlayer.getPlayer().getDisplayName()))
                    .lore(lore)
                    .head(gamePlayer.getPlayer().getUniqueId(), gamePlayer.getPlayer().getName())
                    .onClick(e -> {
                        Player player = (Player) e.getWhoClicked();
                        e.setCancelled(true);
                        if (gamePlayer.getStatus() != GamePlayer.Status.GAMING) {
                            player.sendMessage(MinigamesCore.getPlugin(MinigamesCore.class).getGamePrefix() + langConfig.getPure("spectate-not-gamer"));
                            return;
                        }
                        if (player.getSpectatorTarget() != null) player.setSpectatorTarget(null);
                        player.teleport(gamePlayer.getPlayer().getLocation());
                    })
                    .buildWithSkin(item -> {
                        builder.item(item);
                        this.headMap.put(gamePlayer.getPlayer().getUniqueId(), item);
                    });

        }

        this.spectatorTpInventory = builder.build();
    }


    void removeGamingPlayer(GamePlayer player) {
        if (player.getStatus() == GamePlayer.Status.GAMING) {
            plugin.getLogger().warning("player status of " + player.getPlayer().getName() + " is gaming");
            return;
        }
        Optional.ofNullable(headMap.get(player.getPlayer().getUniqueId())).ifPresent(item -> spectatorTpInventory.remove(item));
    }


    void loadVoteInventory(LobbyManager lobbyManager) throws ArenaNotLoadedException {
        if (lobbyManager.getCandidate().size() == 0) throw new ArenaNotLoadedException();
        ItemConfig.GameItem properties = itemConfig.mapItem;
        Material material = properties.material;
        AbstractInventoryBuilder builder = inventoryGui.getVoteGUI().getKey();
        List<Integer> slots = inventoryGui.getVoteGUI().getValue();
        List<ItemStack> voteItem = new ArrayList<>();
        for (Arena arena : lobbyManager.getCandidate()) {
            String prefix = MinigamesCore.getPlugin(MinigamesCore.class).getGamePrefix();
            String display = ChatColor.translateAlternateColorCodes('&', properties.name.replace("<name>", arena.getDisplayName()));
            List<String> lore = new ArrayList<>(properties.lore);
            if (arena.getDescription().size() == 0) lore.remove(lore.size() - 1);
            else lore.addAll(arena.getDescription());
            List<String> finalLore = lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l.replace("<author>", arena.getAuthor()))).collect(Collectors.toList());
            ItemStack item = new ItemStackBuilder(material).displayName(display).lore(finalLore).onClick(e -> {
                Player player = (Player) e.getWhoClicked();
                playerManager.findPlayer(player).ifPresent(g -> {
                    e.setCancelled(true);
                    try {
                        lobbyManager.vote(g, arena);
                        player.sendMessage(prefix + langConfig.getPure("vote.success"));
                    } catch (AlreadyVotedException ex) {
                        player.sendMessage(prefix + langConfig.getPure("vote.already-voted").replace("<map>", arena.getDisplayName()));
                    }
                });
            }).build();
            voteItem.add(item);
        }

        for (int i = 0; i < Math.min(slots.size(), voteItem.size()); i++) {
            int slot = slots.get(i);
            ItemStack item = voteItem.get(i);
            builder.item(slot, item);
        }

        this.voteInventory = builder.build();
    }

    Inventory getVoteInventory() {
        if (arenaManager.getFinalArena() != null) throw new FinalArenaAlreadyExistException();
        return Optional.ofNullable(voteInventory).orElseThrow(ArenaNotLoadedException::new);
    }

    Inventory getSpectatorTpInventory() {
        return Optional.ofNullable(spectatorTpInventory).orElseThrow(() -> new IllegalStateException("Game has not started, cannot create teleport gui"));
    }


}

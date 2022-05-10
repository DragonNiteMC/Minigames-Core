package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.character.TeamPlayer;
import com.ericlam.mc.minigames.core.config.ItemConfig;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.implement.GlobalTeam;
import com.ericlam.mc.minigames.core.injection.GameItems;
import com.ericlam.mc.minigames.core.main.MinigamesCore;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.builders.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CoreGameItemManager implements GameItemManager {

    private final GlobalTeam globalTeam;
    private final Map<GameTeam, Map<Integer, ItemStack>> gameItemMap;
    private final Map<Integer, ItemStack> lobbyItems;
    private final Map<GameTeam, Map<Integer, ItemStack>> spectatorItemMap;
    private final ItemConfig itemConfig;
    private GameEntry<Integer, ItemStack> voteItem;
    private GameEntry<Integer, ItemStack> specItem;
    private GameEntry<Integer, ItemStack> specInvItem;

    @Inject
    public CoreGameItemManager(GameItems gameItems) {
        this.lobbyItems = gameItems.getLobbyItems();
        this.gameItemMap = gameItems.getGameItemMap();
        this.spectatorItemMap = gameItems.getSpectatorItemMap();
        this.globalTeam = gameItems.getGlobalTeam();
        this.itemConfig = MinigamesCore.getConfigManager().getConfigAs(ItemConfig.class);
    }


    void loadVoteItem(CoreInventoryManager inventoryManager) {
        ItemConfig.GameItem properties = itemConfig.voteItem;
        String display = ChatColor.translateAlternateColorCodes('&', properties.name);
        ItemStack voteItem = new ItemStackBuilder(properties.material)
                .displayName(display)
                .lore(properties.lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()))
                .onInteract(e -> {
                    e.setCancelled(true);
                    if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                    Player player = e.getPlayer();
                    Inventory inv = inventoryManager.getVoteInventory();
                    player.openInventory(inv);
                }).build();
        this.voteItem = new GameEntry<>(properties.slot, voteItem);
    }


    public void loadSpectatorItem(CoreInventoryManager inventoryManager) {
        ItemConfig.GameItem properties = itemConfig.tpItem;
        String display = ChatColor.translateAlternateColorCodes('&', properties.name);
        ItemStack specItem = new ItemStackBuilder(properties.material)
                .displayName(display)
                .lore(properties.lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()))
                .onClick(e -> {
                    Player player = (Player) e.getWhoClicked();
                    e.setCancelled(true);
                    player.openInventory(inventoryManager.getSpectatorTpInventory());
                }).build();
        this.specItem = new GameEntry<>(properties.slot, specItem);
        properties = itemConfig.invItem;
        display = ChatColor.translateAlternateColorCodes('&', properties.name);
        ItemStack specInvItem = new ItemStackBuilder(properties.material)
                .displayName(display)
                .lore(properties.lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()))
                .onClick(e -> {
                    Player spectator = (Player) e.getWhoClicked();
                    e.setCancelled(true);
                    Entity entity = spectator.getSpectatorTarget();
                    if (!(entity instanceof Player)) {
                        spectator.sendMessage("§c你沒有正在第一人稱觀戰的玩家。");
                        return;
                    }
                    spectator.openInventory(((Player) entity).getInventory());
                }).build();
        this.specInvItem = new GameEntry<>(properties.slot, specInvItem);
    }


    void giveJoinItem(GamePlayer player) {
        player.getPlayer().getInventory().setItem(voteItem.getKey(), voteItem.getValue());
        lobbyItems.forEach((i, s) -> player.getPlayer().getInventory().setItem(i, s));
    }

    @Override
    public void giveGameItem(GamePlayer player) {
        GameTeam team = globalTeam;
        if (player instanceof TeamPlayer) {
            team = player.castTo(TeamPlayer.class).getTeam();
        }
        Optional.ofNullable(this.gameItemMap.get(team)).ifPresent(map -> map.forEach((i, s) -> player.getPlayer().getInventory().setItem(i, s)));
    }

    void giveSpectatorItem(GamePlayer player) {
        GameTeam team = globalTeam;
        if (player instanceof TeamPlayer) {
            team = player.castTo(TeamPlayer.class).getTeam();
        }
        player.getPlayer().getInventory().setItem(specItem.getKey(), specItem.getValue());
        player.getPlayer().getInventory().setItem(specInvItem.getKey(), specInvItem.getValue());
        Optional.ofNullable(this.spectatorItemMap.get(team)).ifPresent(map -> map.forEach((i, s) -> player.getPlayer().getInventory().setItem(i, s)));
    }
}

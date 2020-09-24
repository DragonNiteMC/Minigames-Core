package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.registable.Voluntary;
import org.bukkit.inventory.ItemStack;

/**
 * 遊戲物品管理器
 */
public interface GameItemManager {

    /**
     * 給予玩家遊戲時的物品
     *
     * @param player 玩家
     * @see Voluntary#addGameItem(int, ItemStack)
     */
    void giveGameItem(GamePlayer player);

}

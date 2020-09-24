package com.ericlam.mc.minigames.core.registable;

import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.game.InGameState;
import org.bukkit.inventory.ItemStack;

/**
 * 可選註冊器。不註冊并不會導致遊戲無法被激活。
 */
public interface Voluntary {

    /**
     * 註冊大堂倒數和遊戲結束之間的遊戲程序 (順序註冊)
     *
     * @param state 所屬場地狀態
     * @param task  所屬遊戲程序
     */
    void registerGameTask(InGameState state, SectionTask task);

    /**
     * 添加等待投票物品
     *
     * @param slot slot
     * @param item 物品
     */
    void addJoinItem(int slot, ItemStack item);

    /**
     * 添加遊戲內隊伍物品
     *
     * @param team 隊伍
     * @param slot slot
     * @param item 物品
     */
    void addGameItem(GameTeam team, int slot, ItemStack item);

    /**
     * 添加遊戲內非隊伍物品，僅限沒有隊伍玩家
     *
     * @param slot slot
     * @param item 物品
     */
    void addGameItem(int slot, ItemStack item);

    /**
     * 添加觀戰隊伍物品
     *
     * @param team 隊伍
     * @param slot slot
     * @param item 物品
     */
    void addSpectatorItem(GameTeam team, int slot, ItemStack item);

    /**
     * 添加觀戰非隊伍物品, 僅限沒有隊伍玩家
     *
     * @param slot slot
     * @param item 物品
     */
    void addSpectatorItem(int slot, ItemStack item);

}

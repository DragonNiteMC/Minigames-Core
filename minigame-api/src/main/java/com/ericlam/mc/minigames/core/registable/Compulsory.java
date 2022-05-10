package com.ericlam.mc.minigames.core.registable;

import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.arena.ArenaMechanic;
import com.ericlam.mc.minigames.core.character.GamePlayerHandler;
import com.ericlam.mc.minigames.core.gamestats.GameStatsHandler;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractInventoryBuilder;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * 強制註冊類，必須註冊所有遊戲才會被激活。
 */
public interface Compulsory {

    /**
     * 註冊大堂倒數程序
     *
     * @param task 遊戲程序
     */
    void registerLobbyTask(SectionTask task);

    /**
     * 註冊遊戲結束程序
     *
     * @param task 遊戲程序
     */
    void registerEndTask(SectionTask task);

    /**
     * 註冊場地創建主指令, 創建後將會自動幫你把 {@link com.ericlam.mc.minigames.core.manager.ArenaCreateManager} 的操作全部加到這個主指令內。
     *
     * @param defaultCommand 根指令
     * @param plugin         插件實例
     */
    void registerArenaCommand(DefaultCommand defaultCommand, final JavaPlugin plugin);

    /**
     * 註冊場地設定
     *
     * @param arenaConfig 場地設定
     */
    void registerArenaConfig(@Nonnull ArenaConfig arenaConfig);

    /**
     * 註冊場地設置機制
     *
     * @param arenaMechanic 場地設置機制
     */
    void registerArenaMechanic(@Nonnull ArenaMechanic arenaMechanic);

    /**
     * 註冊遊戲玩家管理機制
     *
     * @param gamePlayerHandler 遊戲玩家管理機制
     */
    void registerGamePlayerHandler(@Nonnull GamePlayerHandler gamePlayerHandler);

    /**
     * 註冊遊戲玩家資料管理機制
     *
     * @param gameStatsHandler 遊戲玩家資料管理機制
     */
    void registerGameStatsHandler(@Nonnull GameStatsHandler gameStatsHandler);

    /**
     * 註冊投票界面
     *
     * @param inventoryBuilder 背包建造器
     * @param allowSlot        地圖投票物品 slot
     */
    void registerVoteGUI(AbstractInventoryBuilder inventoryBuilder, Integer... allowSlot);
}

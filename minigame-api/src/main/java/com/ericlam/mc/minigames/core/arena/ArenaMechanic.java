package com.ericlam.mc.minigames.core.arena;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * 讓使用者註冊的場地設置機制
 */
public interface ArenaMechanic {

    /**
     * 定義如何加載場地
     *
     * @param yml  場地文件
     * @param info 場地基本資訊
     * @return Arena 實作容器
     */
    CreateArena loadCreateArena(FileConfiguration yml, Arena info);

    /**
     * 定義如何創建場地
     *
     * @param name   場地名稱
     * @param player 創建該場地的玩家
     * @return 新的 Arena 實作容器
     */
    CreateArena createArena(@Nonnull final String name, @Nonnull final Player player);

    /**
     * 保存額外的場地設定
     *
     * @param preSaveYml 即將會被儲存的場地文件
     * @param arena      Arena 容器
     */
    void saveExtraArenaSetting(FileConfiguration preSaveYml, Arena arena);

}

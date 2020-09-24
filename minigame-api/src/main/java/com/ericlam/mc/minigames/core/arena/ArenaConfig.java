package com.ericlam.mc.minigames.core.arena;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * 用於被使用者實作來註冊場地機制運作
 */
public interface ArenaConfig {

    /**
     * @return 場地裝載資料夾
     */
    File getArenaFolder();

    /**
     * @return 最大場地加載數(等待中時)
     */
    int getMaxLoadArena();

    /**
     * @param world 世界設置
     */
    void setExtraWorldSetting(@Nonnull World world);

    /**
     * 獲得可設置的地標名稱和最大設置數
     * <p>
     * String 是地標名稱, Integer 是該地標最大設置數
     *
     * @return 允許的地標
     */
    ImmutableMap<String, Integer> getAllowWarps();

    /**
     * @return 等候房間位置
     */
    Location getLobbyLocation();

    /**
     * 當遊戲結束後自動回傳的伺服器
     *
     * @return 返回伺服器的名稱
     */
    String getFallBackServer();

    /**
     * @return 此小遊戲的前綴
     */
    String getGamePrefix();

    /**
     * 如何儲存等候房間位置到你的 yaml
     *
     * @param location 等候房間位置
     * @return 設置是否成功
     */
    CompletableFuture<Boolean> setLobbyLocation(final Location location);

}

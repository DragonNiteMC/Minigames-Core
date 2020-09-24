package com.ericlam.mc.minigames.core.arena;

import com.ericlam.mc.minigames.core.function.Castable;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

/**
 * 場地接口，是場地API的主體
 */
public interface Arena extends Castable<Arena> {

    /**
     * @return 場地作者
     */
    String getAuthor();

    /**
     * @return 場地世界
     */
    World getWorld();

    /**
     * @return 場地名稱
     */
    String getArenaName();

    /**
     * @return 顯示名稱
     */
    String getDisplayName();

    /**
     * @return 場地的地標列表
     */
    Map<String, List<Location>> getLocationsMap();

    /**
     * @param warp 地標
     * @return 位置列表
     */
    default List<Location> getWarp(String warp) {
        return getLocationsMap().get(warp);
    }

    /**
     * @return 場地描述
     */
    List<String> getDescription();

    /**
     * @return 場地資訊
     */
    String[] getInfo();

}

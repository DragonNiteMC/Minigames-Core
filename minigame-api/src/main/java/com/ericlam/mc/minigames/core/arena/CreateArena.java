package com.ericlam.mc.minigames.core.arena;

import com.ericlam.mc.minigames.core.exception.NoMoreElementException;
import com.ericlam.mc.minigames.core.exception.arena.create.LocationMaxReachedException;
import com.ericlam.mc.minigames.core.exception.arena.create.NoMoreLocationException;
import com.ericlam.mc.minigames.core.exception.arena.create.WarpExistException;
import com.ericlam.mc.minigames.core.exception.arena.create.WarpNotExistException;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 設置場地接口, 用於設置場地
 */
public interface CreateArena extends Arena {

    /**
     * @param author 場地作者
     */
    void setAuthor(String author);

    /**
     * @param world 場地世界
     */
    void setWorld(World world);

    /**
     * @param arenaName 場地名稱
     */
    void setArenaName(String arenaName);

    /**
     * @param displayName 場地顯示名稱
     */
    void setDisplayName(String displayName);

    /**
     * @param warp     地標
     * @param location 位置
     * @param max      最大設置數
     * @throws WarpNotExistException       地標不存在時
     * @throws LocationMaxReachedException 地標位置數過多時
     */
    default void addLocation(String warp, Location location, final int max) throws WarpNotExistException, LocationMaxReachedException {
        List<Location> locs = Optional.ofNullable(getWarp(warp)).orElseThrow(() -> new WarpNotExistException(warp));
        if (locs.size() >= max) throw new LocationMaxReachedException(warp);
        locs.add(location);
    }

    /**
     * 刪除上一個地標
     *
     * @param warp 地標
     * @throws WarpNotExistException   地標不存在時
     * @throws NoMoreLocationException 沒有更多位置時
     */
    default void removeLastLocation(String warp) throws WarpNotExistException, NoMoreLocationException {
        List<Location> locs = Optional.ofNullable(getWarp(warp)).orElseThrow(() -> new WarpNotExistException(warp));
        if (locs.size() == 0) throw new NoMoreLocationException(warp);
        locs.remove(locs.size() - 1);
    }

    /**
     * @param warp 添加地標
     * @throws WarpExistException 地標已存在時
     */
    default void addWarp(String warp) throws WarpExistException {
        if (getLocationsMap().putIfAbsent(warp, new LinkedList<>()) != null) throw new WarpExistException(warp);
    }

    /**
     * @param warp 刪除地標
     * @throws WarpNotExistException 地標不存在時
     */
    default void removeWarp(String warp) throws WarpNotExistException {
        if (getLocationsMap().remove(warp) == null) throw new WarpNotExistException(warp);
    }

    /**
     * 為描述添加新一行
     *
     * @param text 描述文字
     */
    default void addDescriptionLine(String text) {
        getDescription().add(text);
    }

    /**
     * 刪除上一行描述
     *
     * @throws NoMoreElementException 沒有更多描述時
     */
    default void removeDescriptionLine() throws NoMoreElementException {
        if (getDescription().size() == 0) throw new NoMoreElementException(this.getArenaName());
        getDescription().remove(getDescription().size() - 1);
    }

    /**
     * @param locationMap 地標列表
     */
    void setLocationMap(Map<String, List<Location>> locationMap);

    /**
     * @return 場地有否被變更
     */
    boolean isChanged();

    /**
     * 設置場地有否被變更
     *
     * @param changed 有否被變更
     */
    void setChanged(Boolean changed);

    /**
     * 定義場地完成設置的條件
     *
     * @return 是否完成設置
     */
    boolean isSetupCompleted();
}

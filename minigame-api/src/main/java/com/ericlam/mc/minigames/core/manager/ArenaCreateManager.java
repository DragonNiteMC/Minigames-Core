package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.arena.CreateArena;
import com.ericlam.mc.minigames.core.exception.NoMoreElementException;
import com.ericlam.mc.minigames.core.exception.arena.create.*;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 場地設置管理器
 */
public interface ArenaCreateManager {

    /**
     * 設置作者
     *
     * @param arena  場地
     * @param author 作者
     * @throws ArenaNotExistException 場地不存在時
     */
    void setAuthor(String arena, String author) throws ArenaNotExistException;

    /**
     * 設置顯示名稱
     *
     * @param arena       場地
     * @param displayName 顯示名稱
     * @throws ArenaNotExistException 場地不存在時
     */
    void setDisplayName(String arena, String displayName) throws ArenaNotExistException;

    /**
     * 設置場地名稱
     *
     * @param arena   場地
     * @param newName 名稱
     * @throws ArenaNotExistException  場地不存在時
     * @throws ArenaNameExistException 場地名與其他場地重複時
     */
    void setName(String arena, String newName) throws ArenaNotExistException, ArenaNameExistException;

    /**
     * 添加位置
     *
     * @param arena    場地
     * @param warp     地標
     * @param location 新位置
     * @throws ArenaNotExistException      場地不存在時
     * @throws WarpNotExistException       地標不存在時
     * @throws LocationMaxReachedException 最大設置數達到時
     */
    void addSpawn(String arena, String warp, Location location) throws ArenaNotExistException, WarpNotExistException, LocationMaxReachedException;

    /**
     * 刪除上一個位置
     *
     * @param arena 場地
     * @param warp  地標
     * @throws ArenaNotExistException  場地不存在時
     * @throws WarpNotExistException   地標不存在時
     * @throws NoMoreLocationException 沒有更多位置時
     */
    void removeSpawn(String arena, String warp) throws ArenaNotExistException, WarpNotExistException, NoMoreLocationException;

    /**
     * 新增地標
     *
     * @param arena 場地
     * @param warp  新地標
     * @throws ArenaNotExistException 場地不存在時
     * @throws WarpExistException     地標已存在時
     * @throws IllegalWarpException   地標不符合規範時
     */
    void createWarp(String arena, String warp) throws ArenaNotExistException, WarpExistException, IllegalWarpException;

    /**
     * 刪除地標
     *
     * @param arena 場地
     * @param warp  地標
     * @throws ArenaNotExistException 場地不存在時
     * @throws WarpNotExistException  地標不存在時
     */
    void removeWarp(String arena, String warp) throws ArenaNotExistException, WarpNotExistException;

    /**
     * 保存場地
     *
     * @param arena 場地
     * @return 異步保存
     * @throws SetUpNotFinishException 設置尚未完成時
     * @throws ArenaNotExistException  場地不存在時
     * @throws ArenaUnchangedExcpetion 場地沒有被變更時
     * @throws ArenaNotBackupException 場地尚未備份時
     */
    CompletableFuture<Boolean> saveArena(String arena) throws SetUpNotFinishException, ArenaNotExistException, ArenaUnchangedExcpetion, ArenaNotBackupException;


    /**
     * @param arena 場地
     * @return 異步備份地圖
     * @throws ArenaNotExistException    場地不存在時
     * @throws BackupNotAllowedException 遊戲場地恢復機制未被啟用時
     */
    CompletableFuture<File> backupArena(String arena) throws ArenaNotExistException, BackupNotAllowedException;

    /**
     * 獲取場地資訊
     *
     * @param arena 場地
     * @return 資訊列
     * @throws ArenaNotExistException 場地不存在時
     */
    String[] getArenaInfo(String arena) throws ArenaNotExistException;

    /**
     * 刪除場地
     *
     * @param arena 場地
     * @return 異步運作
     * @throws ArenaNotExistException 場地不存在時
     */
    CompletableFuture<Boolean> deleteArena(String arena) throws ArenaNotExistException;

    /**
     * 創建場地
     *
     * @param name   場地名稱
     * @param player 玩家
     * @throws ArenaExistException 場地名稱已存在時
     */
    void createArena(String name, Player player) throws ArenaExistException;

    /**
     * 獲取該世界內存在的場地列
     *
     * @param world 世界
     * @return 該世界內存在的場地列
     */
    Arena[] getArenasFromWorld(World world);

    /**
     * 獲取所有場地列表
     *
     * @return 無法被變更的場地列表
     */
    ImmutableList<Arena> getArenaList();

    /**
     * 獲取地標列表
     *
     * @param arena 場地
     * @return 地標列表
     * @throws ArenaNotExistException 場地不存在時
     */
    List<String> getWarpList(String arena) throws ArenaNotExistException;

    /**
     * 獲取該場地的編輯容器
     *
     * @param arena 場地
     * @return 可編輯的場地容器
     * @throws ArenaNotExistException 場地不存在時
     */
    CreateArena getCreateArena(String arena) throws ArenaNotExistException;

    /**
     * 新增場地描述
     *
     * @param arena 場地
     * @param text  文字內容
     * @throws ArenaNotExistException 場地不存在時
     */
    void addDescriptionLine(String arena, String text) throws ArenaNotExistException;

    /**
     * 刪除上一個場地描述
     *
     * @param arena 場地
     * @throws NoMoreElementException 沒有更多描述時
     * @throws ArenaNotExistException 場地不存在時
     */
    void removeDescriptionLine(String arena) throws NoMoreElementException, ArenaNotExistException;
}

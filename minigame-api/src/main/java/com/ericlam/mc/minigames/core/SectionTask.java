package com.ericlam.mc.minigames.core;

import com.ericlam.mc.minigames.core.manager.PlayerManager;

/**
 * 遊戲運行程序接口
 */
public interface SectionTask {

    /**
     * 定義初始化時的動作
     *
     * @param playerManager 玩家管理器
     */
    void initTimer(PlayerManager playerManager);

    /**
     * 在取消程序時運行
     */
    void onCancel();

    /**
     * 在完成程序時運行
     */
    void onFinish();

    /**
     * 每秒的運行程序
     *
     * @param remain 剩餘秒數
     * @return 最終的剩餘秒數
     */
    long run(long remain);

    /**
     * 獲取這個運行程序的總秒數
     *
     * @return 總秒數
     */
    long getTotalTime();

    /**
     * 取消此運行程序的條件
     *
     * @return 取消此運行程序的條件
     */
    boolean shouldCancel();

    /**
     * 判斷程序是否在運行
     *
     * @return 是否在運行
     */
    boolean isRunning();

    /**
     * 設置程序是否在運行
     *
     * @param running 是否在運行
     */
    void setRunning(boolean running);
}

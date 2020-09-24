package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.exception.task.TaskAlreadyRunningException;
import com.ericlam.mc.minigames.core.game.InGameState;

import javax.annotation.Nullable;

/**
 * 排程管理器
 */
public interface ScheduleManager {

    /**
     * 運行初始程序 (大堂倒數)
     *
     * @param forceStart 是否為強制開始 (無法取消 + 倒數只有十秒)
     */
    void startFirst(boolean forceStart);

    /**
     * 開始該場地狀態的程序
     *
     * @param state 場地狀態
     * @throws TaskAlreadyRunningException 該程序已在運行時
     */
    void start(InGameState state) throws TaskAlreadyRunningException;

    /**
     * 該程序是否運行中
     *
     * @param state 遊戲狀態
     * @return 是否運行中
     */
    boolean isRunning(InGameState state);

    /**
     * 取消目前程序
     */
    void cancelCurrent();

    /**
     * 強制完成目前程序
     */
    void finishCurrent();

    /**
     * 跳到某程序
     *
     * @param state  遊戲狀態
     * @param cancel 強制取消, 否則強制完成
     */
    void jumpInto(InGameState state, boolean cancel);

    /**
     * 返回目前場地狀態
     *
     * @return 目前場地狀態
     */
    @Nullable
    InGameState getCurrentGameState();

}

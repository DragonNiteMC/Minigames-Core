package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 遊戲內工具類
 */
public interface GameUtils {

    /**
     * 根據秒數獲取 時間顯示文字: x 時 x 分 x秒
     *
     * @param sec 秒
     * @return 時間顯示文字
     */
    String getTimeWithUnit(long sec);

    /**
     * 根據秒數獲取 時間顯示格式: 00:00
     *
     * @param sec 秒
     * @return 時間顯示格式: 00:00
     */
    String getTimer(long sec);

    /**
     * 播放聲效
     * <ul>
     *     <li>[0] 為聲效(可自定義)</li>
     *     <li>[1] 為 pitch</li>
     *     <li>[2] 為 volume</li>
     * </ul>
     *
     * @param player      玩家
     * @param soundString 聲音文字，格式如上
     */
    void playSound(Player player, String[] soundString);

    /**
     * 逐步循環操作
     *
     * @param collection 集合物件
     * @param task       對每個物件的迭代處理
     * @param period     每次處理間隔
     * @param <T>        物件形態
     */
    <T> void unLagIterate(Collection<T> collection, Consumer<T> task, long period);

    /**
     * 逐步循環傳送
     *
     * @param gamePlayers 遊戲玩家列表
     * @param locations   傳送位置列表
     * @param period      每次傳送間隔
     */
    void noLagTeleport(List<GamePlayer> gamePlayers, List<Location> locations, long period);

    /**
     * 逐步循環傳送
     *
     * @param gamePlayers 遊戲玩家列表
     * @param period      傳送位置列表
     * @param locations   每次傳送間隔
     */
    void noLagTeleport(List<GamePlayer> gamePlayers, long period, Location... locations);
}

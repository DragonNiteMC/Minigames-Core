package com.ericlam.mc.minigames.core.factory.scoreboard;

import com.ericlam.mc.minigames.core.character.GamePlayer;
import com.ericlam.mc.minigames.core.factory.Factory;
import com.ericlam.mc.minigames.core.game.GameTeam;
import org.bukkit.scoreboard.Team;

import java.util.function.BiFunction;

/**
 * 計分版工廠
 */
public interface ScoreboardFactory extends Factory<GameBoard> {

    /**
     * 添加隊伍設定
     *
     * @param gameTeam 隊伍
     * @param option   設定
     * @param status   狀態
     * @return this
     */
    ScoreboardFactory addTeamSetting(GameTeam gameTeam, Team.Option option, Team.OptionStatus status);

    /**
     * 添加全局設定
     * <p>
     * 注意，此設定僅限能套用在<b>沒有隊伍</b>的玩家。
     *
     * @param option 設定
     * @param status 狀態
     * @return this
     */
    ScoreboardFactory addSetting(Team.Option option, Team.OptionStatus status);

    /**
     * 設置計分版標題, 支援顏色代碼
     *
     * @param title 標題
     * @return this
     */
    ScoreboardFactory setTitle(String title);

    /**
     * 設置計分版內容
     *
     * @param key   此行內容的標識文字
     * @param text  文字, 支援顏色
     * @param score 分數
     * @return this
     */
    ScoreboardFactory setLine(String key, String text, int score);

    /**
     * 添加計分版內容而不標識文字
     *
     * @param text  文字, 支援顏色
     * @param score 分數
     * @return this
     */
    ScoreboardFactory addLine(String text, int score);


    /**
     * 添加個人的計分版內容(每個玩家顯示的文字都不同)
     *
     * @param text    標識文字, 支援顏色
     * @param score   分數
     * @param applier 根據 玩家 和 標識文字 最後產生的 顯示文字
     * @return this
     */
    ScoreboardFactory addLine(String text, int score, BiFunction<GamePlayer, String, String> applier);

    /**
     * 個人計分版內容更新頻率，默認為10秒, 最低為 10 ticks
     *
     * @param ticks 更新頻率
     * @return this
     */
    ScoreboardFactory setUpdateInterval(long ticks);


}

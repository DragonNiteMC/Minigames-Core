package com.ericlam.mc.minigames.core.factory.compass;

import com.ericlam.mc.minigames.core.factory.Factory;
import com.ericlam.mc.minigames.core.game.GameTeam;

/**
 * 羅盤追蹤器工廠
 * <p>
 * 如果沒有隊伍，則會追蹤最近的一位玩家
 * </p>
 * <p>
 * 否則，追蹤目標隊伍最近的一位玩家
 * </p>
 */
public interface CompassFactory extends Factory<CompassTracker> {

    /**
     * 設置隊伍追蹤目標
     *
     * @param team   隊伍
     * @param target 目標隊伍
     * @return this
     */
    CompassFactory setTeamTarget(GameTeam team, GameTeam target);

    /**
     * 設置追蹤範圍
     *
     * @param range 範圍
     * @return this
     */
    CompassFactory setTrackerRange(int range);

    /**
     * 設置搜索文字標題動畫
     *
     * @param text 搜索文字標題動畫
     * @return this
     */
    CompassFactory setSearchingText(String... text);

    /**
     * 設置捕獲到目標時的指南針標題
     * <p>
     * 佔位符如下:
     * <ul>
     *     <li> &lt;target&gt; - targetName </li>
     *     <li> &lt;distance&gt; - distance between target </li>
     *     <li> &lt;team&gt; - teamName </li>
     * </ul>
     *
     * @param caughtText 捕獲到目標時的指南針標題
     * @return this;
     */
    CompassFactory setCaughtText(String caughtText);

}

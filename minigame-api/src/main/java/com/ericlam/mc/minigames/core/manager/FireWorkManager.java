package com.ericlam.mc.minigames.core.manager;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;
import java.util.Random;

/**
 * 煙火管理器
 */
public interface FireWorkManager {

    /**
     * 獲取煙火設定
     *
     * @param firework 煙火
     * @return 煙火設定
     */
    FireworkMeta getFirework(Firework firework);

    /**
     * 獲取小煙火設定
     *
     * @param firework 煙火
     * @return 煙火設定
     */
    FireworkMeta getQuickFirework(Firework firework);

    /**
     * 獲取隨機顏色
     *
     * @param random 隨機
     * @return 顏色
     */
    Color getRandomColor(Random random);

    /**
     * 生成煙火
     *
     * @param player 生成位置
     */
    void spawnFireWork(Player player);

    /**
     * 生成一連串煙火
     *
     * @param locations 一連串的位置
     */
    void spawnFireWork(List<Location> locations);

}

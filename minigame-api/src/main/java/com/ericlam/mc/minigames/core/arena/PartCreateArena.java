package com.ericlam.mc.minigames.core.arena;

import org.bukkit.util.BlockVector;

/**
 * 區域場地的設置接口
 */
public interface PartCreateArena extends CreateArena, PartArena {

    /**
     * 設置頂端位置
     *
     * @param vector 區域頂端位置
     */
    void setTopCorner(BlockVector vector);

    /**
     * 設置低端位置
     *
     * @param vector 區域低端位置
     */
    void setBottomCorner(BlockVector vector);

}

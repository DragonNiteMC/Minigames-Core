package com.ericlam.mc.minigames.core.arena;

import org.bukkit.util.BlockVector;

/**
 * 需要標註區域範圍的場地專用接口
 * <p>
 * 頂端位置與低端位置，將會形成一個長方體的場地區域。
 */
public interface PartArena extends Arena {

    /**
     * @return 場地區域最頂端的位置
     */
    BlockVector getTopCorner();

    /**
     * @return 場地區域最低端的位置
     */
    BlockVector getBottomCorner();

}

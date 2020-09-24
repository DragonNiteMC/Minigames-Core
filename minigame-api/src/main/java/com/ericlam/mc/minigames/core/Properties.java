package com.ericlam.mc.minigames.core;

import com.ericlam.mc.minigames.core.factory.GameFactory;
import com.hypernite.mc.hnmc.core.config.MessageGetter;

/**
 * 獲取其他設定
 */
public interface Properties {

    /**
     * 獲取 Minigames-Core 內的 Message 管理器
     *
     * @return 獲取 Message 管理器
     */
    MessageGetter getMessageGetter();

    /**
     * 獲取遊戲工廠
     *
     * @return 遊戲工廠
     */
    GameFactory getGameFactory();


}

package com.ericlam.mc.minigames.core.registable;

/**
 * 遊戲註冊器
 */
public interface Registration {

    /**
     * 獲取強制性註冊器
     *
     * @return 強制性註冊器
     */
    Compulsory getCompulsory();

    /**
     * 獲取可選註冊器
     *
     * @return 可選註冊器
     */
    Voluntary getVoluntary();

}

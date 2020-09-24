package com.ericlam.mc.minigames.core.function;

/**
 * 可以被形態轉換的容器
 *
 * @param <E> 根形態
 */
public interface Castable<E> {

    /**
     * 轉換形態到子容器
     * <p>
     * 相當於
     * <code>
     * Child child = (Child)root;
     * </code>
     *
     * @param cls 子容器類
     * @param <T> 容器形態
     * @return 子容器
     */
    default <T extends E> T castTo(Class<T> cls) {
        return cls.cast(this);
    }

}

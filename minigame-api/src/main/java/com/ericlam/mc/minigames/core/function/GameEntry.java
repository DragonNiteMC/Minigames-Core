package com.ericlam.mc.minigames.core.function;

import java.util.Map;

/**
 * 雙子容器
 *
 * @param <K> 第一類
 * @param <V> 第二類
 */
public final class GameEntry<K, V> implements Map.Entry<K, V> {


    private final K k;
    private V v;

    public GameEntry(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public static <K, V> GameEntry of(K k, V v) {
        return new GameEntry<>(k, v);
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V value) {
        this.v = value;
        return v;
    }
}

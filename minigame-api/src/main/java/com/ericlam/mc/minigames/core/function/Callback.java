package com.ericlam.mc.minigames.core.function;

@FunctionalInterface
public interface Callback<V> {
    void done(V v, Throwable throwable);
}

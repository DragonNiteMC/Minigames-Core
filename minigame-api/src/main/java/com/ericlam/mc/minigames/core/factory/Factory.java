package com.ericlam.mc.minigames.core.factory;

@FunctionalInterface
public interface Factory<T> {
    T build();
}

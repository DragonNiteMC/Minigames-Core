package com.ericlam.mc.minigames.core;

import com.ericlam.mc.minigames.core.manager.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.util.HashMap;
import java.util.Map;

public final class MinigamesModule implements Module {

    private final Map<Class, Object> binder = new HashMap<>();
    private final Map<Class, Class<?>> bindercls = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Binder binder) {
        binder.bind(FireWorkManager.class).to(CoreFireWorkManager.class).in(Scopes.SINGLETON);
        binder.bind(GameManager.class).to(CoreGameManager.class).in(Scopes.SINGLETON);
        binder.bind(ArenaManager.class).to(CoreArenaManager.class).in(Scopes.SINGLETON);
        binder.bind(LobbyManager.class).to(CoreLobbyManager.class).in(Scopes.SINGLETON);
        binder.bind(ScheduleManager.class).to(CoreScheduleManager.class).in(Scopes.SINGLETON);
        binder.bind(ArenaCreateManager.class).to(CoreArenaCreateManager.class).in(Scopes.SINGLETON);
        binder.bind(GameItemManager.class).to(CoreGameItemManager.class).in(Scopes.SINGLETON);
        binder.bind(InventoryManager.class).to(CoreInventoryManager.class).in(Scopes.SINGLETON);
        binder.bind(GameStatsManager.class).to(CoreGameStatsManager.class).in(Scopes.SINGLETON);
        binder.bind(PlayerManager.class).to(CorePlayerManager.class).in(Scopes.SINGLETON);
        binder.bind(GameUtils.class).to(CoreGameUtils.class).in(Scopes.SINGLETON);

        this.binder.forEach((k, v) -> binder.bind(k).toInstance(v));
        this.bindercls.forEach((k, v) -> binder.bind(k).to(v).in(Scopes.SINGLETON));
    }

    public void register(Class<?> zls, Object instance) {
        this.binder.put(zls, instance);
    }

    public void register(Class<?> zls, Class<?> cls) {
        this.bindercls.put(zls, cls);
    }
}

package com.ericlam.mc.minigames.core.config;

import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;
import java.util.Map;

@Resource(locate = "config.yml")
public final class MGConfig extends Configuration {

    public boolean allowMultiArenaInOneWorld;

    public boolean lunchGameOnStart;

    public InteractInventory interactInventory;

    public DamageMultiplier damageMultiplier;

    public long forceStartTime;

    public Map<String, Boolean> gameStatsControl;

    public static class DamageMultiplier {
        public boolean disable;
        public double maxDamage;
    }

    public static class InteractInventory {
        public boolean whitelist;
        public List<InventoryType> list;
    }
}

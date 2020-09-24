package com.ericlam.mc.minigames.core.config;

import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;
import org.bukkit.Material;

import java.util.List;

@Resource(locate = "items.yml")
public class ItemConfig extends Configuration {

    public GameItem voteItem;

    public GameItem mapItem;

    public GameItem tpItem;

    public GameItem headItem;

    public GameItem invItem;

    public static class GameItem {
        public Material material;
        public int slot;
        public String name;
        public List<String> lore;
    }
}

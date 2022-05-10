package com.ericlam.mc.minigames.core.config;

import com.dragonite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonite.mc.dnmc.core.config.yaml.Resource;

@Resource(locate = "backups.yml")
public class BackupConfig extends Configuration {

    public boolean arenaBackupEnabled;

    public String lastLoadedArena;

}

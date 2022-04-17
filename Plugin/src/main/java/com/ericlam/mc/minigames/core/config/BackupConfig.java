package com.ericlam.mc.minigames.core.config;

import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;

@Resource(locate = "backups.yml")
public class BackupConfig extends Configuration {

    public boolean arenaBackupEnabled;

    public String lastLoadedArena;

}

package com.ericlam.mc.minigames.core.config;

import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;

@Resource(locate = "backups.yml")
public class BackupConfig extends Configuration {

    public boolean arenaBackupEnabled;

    public String lastLoadedArena;

}

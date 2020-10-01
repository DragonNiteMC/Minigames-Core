package com.ericlam.mc.minigames.core.main;

import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.MinigamesModule;
import com.ericlam.mc.minigames.core.Properties;
import com.ericlam.mc.minigames.core.SectionTask;
import com.ericlam.mc.minigames.core.arena.ArenaConfig;
import com.ericlam.mc.minigames.core.commands.MinigameCommand;
import com.ericlam.mc.minigames.core.config.BackupConfig;
import com.ericlam.mc.minigames.core.config.ItemConfig;
import com.ericlam.mc.minigames.core.config.LangConfig;
import com.ericlam.mc.minigames.core.config.MGConfig;
import com.ericlam.mc.minigames.core.exception.APINotActivatedException;
import com.ericlam.mc.minigames.core.factory.CoreGameFactory;
import com.ericlam.mc.minigames.core.factory.GameFactory;
import com.ericlam.mc.minigames.core.function.GameEntry;
import com.ericlam.mc.minigames.core.game.GameState;
import com.ericlam.mc.minigames.core.game.GameTeam;
import com.ericlam.mc.minigames.core.injection.GameItems;
import com.ericlam.mc.minigames.core.injection.GameProgramTasks;
import com.ericlam.mc.minigames.core.injection.InventoryGui;
import com.ericlam.mc.minigames.core.manager.*;
import com.ericlam.mc.minigames.core.registable.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hypernite.mc.hnmc.core.config.MessageGetter;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CommandRegister;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import com.hypernite.mc.hnmc.core.managers.builder.AbstractInventoryBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;

public final class MinigamesCore extends JavaPlugin implements MinigamesAPI, Registration, Properties, Listener {

    private static MinigamesAPI api;
    private static Registration reg;
    private static Properties pro;
    private static YamlManager configManager;
    private static boolean packetWrapperEnabled = false;
    private static boolean crackShotPlusEnabled = false;
    private final MinigamesModule minigamesModule = new MinigamesModule();
    private final VoluntaryRegistration voluntary = new VoluntaryRegistration();
    private final GameFactory gameFactory = new CoreGameFactory();
    private CompulsoryRegistration compulsory;
    private FireWorkManager fireWorkManager;
    private GameManager gameManager;
    private MessageGetter messageGetter;
    private LobbyManager lobbyManager;
    private ScheduleManager scheduleManager;
    private ArenaManager arenaManager;
    private ArenaCreateManager arenaCreateManager;
    private GameItemManager gameItemManager;
    private PlayerManager playerManager;
    private ArenaConfig arenaConfig;
    private GameStatsManager gameStatsManager;
    private GameUtils gameUtils;
    private CoreInventoryManager coreInventoryManager;

    public static MinigamesAPI getApi() {
        return Optional.ofNullable(api).orElseThrow(APINotActivatedException::new);
    }

    public static Optional<MinigamesAPI> getApiSafe() {
        return Optional.ofNullable(api);
    }

    public static Registration getRegistration() {
        return reg;
    }

    public static YamlManager getConfigManager() {
        return configManager;
    }

    public static Properties getProperties() {
        return pro;
    }

    public static boolean isPacketWrapperEnabled() {
        return packetWrapperEnabled;
    }

    public static boolean isCrackShotPlusEnabled() {
        return crackShotPlusEnabled;
    }

    @Override
    public void onLoad() {
        reg = this;
        pro = this;
        configManager = HyperNiteMC.getAPI().getFactory().getConfigFactory(this)
                .register("config.yml", MGConfig.class)
                .register("items.yml", ItemConfig.class)
                .register("lang.yml", LangConfig.class)
                .register("backups.yml", BackupConfig.class)
                .dump();
        messageGetter = new MessageGetterImpl(configManager.getConfigAs(LangConfig.class));
        minigamesModule.register(Plugin.class, this);
        compulsory = new CompulsoryRegistration(minigamesModule, configManager);
    }

    @Override
    public void onEnable() {
        packetWrapperEnabled = getServer().getPluginManager().isPluginEnabled("PacketWrapper");
        crackShotPlusEnabled = getServer().getPluginManager().isPluginEnabled("CrackShotPlus");
        this.getServer().getScheduler().runTask(this, () -> {
            this.getLogger().info("Initializing Minigames-Api...");
            GameEntry<AbstractInventoryBuilder, List<Integer>> voteGUI = compulsory.getVoteGUI();
            SectionTask lobbyTask = compulsory.getLobbyTask();
            SectionTask endTask = compulsory.getEndTask();
            boolean pmRegistered = compulsory.isGamePlayerHandlerRegistered();
            boolean amRegistered = compulsory.isArenaMechanicRegistered();
            boolean acRegistered = compulsory.isArenaConfigRegistered();
            boolean gshRegistered = compulsory.isGameStatsHandlerRegistered();
            boolean launch = true;
            if (voteGUI == null) {
                this.getLogger().warning("VoteGUI is not registered.");
                launch = false;
            }
            if (lobbyTask == null) {
                this.getLogger().warning("No Lobby Task has been registered.");
                launch = false;
            }
            if (endTask == null) {
                this.getLogger().warning("No PreEndTask has been registered.");
                launch = false;
            }
            if (!pmRegistered) {
                this.getLogger().warning("No GamePlayerHandler registered.");
                launch = false;
            }
            if (!amRegistered) {
                this.getLogger().warning("No ArenaMechanic registered");
                launch = false;
            }
            if (!acRegistered) {
                this.getLogger().warning("No ArenaConfig registered");
                launch = false;
            }

            if (!gshRegistered) {
                this.getLogger().warning("No GameStatsHandler registered");
                launch = false;
            }
            if (!launch) {
                this.getLogger().warning("API will not be activated");
                return;
            }
            api = this;
            InventoryGui gui = new InventoryGui(voteGUI);
            GameProgramTasks tasks = new GameProgramTasks(lobbyTask, endTask, voluntary.getGameTasks());
            GameItems gameItems = new GameItems(voluntary.getGlobalTeam(), voluntary.getLobbyItems(), voluntary.getGameItemMap(), voluntary.getSpectatorItemMap());
            minigamesModule.register(GameProgramTasks.class, tasks);
            minigamesModule.register(GameItems.class, gameItems);
            minigamesModule.register(InventoryGui.class, gui);

            Injector injector = Guice.createInjector(minigamesModule);

            fireWorkManager = injector.getInstance(FireWorkManager.class);
            gameManager = injector.getInstance(GameManager.class);
            arenaCreateManager = injector.getInstance(ArenaCreateManager.class);
            lobbyManager = injector.getInstance(LobbyManager.class);
            scheduleManager = injector.getInstance(ScheduleManager.class);
            arenaManager = injector.getInstance(ArenaManager.class);
            gameItemManager = injector.getInstance(GameItemManager.class);
            playerManager = injector.getInstance(PlayerManager.class);
            arenaConfig = injector.getInstance(ArenaConfig.class);
            gameStatsManager = injector.getInstance(GameStatsManager.class);
            gameUtils = injector.getInstance(GameUtils.class);
            coreInventoryManager = (CoreInventoryManager) injector.getInstance(InventoryManager.class);

            this.getLogger().info("API initialized and Activated");
            this.getServer().getPluginManager().registerEvents(this, this);
            CoreGameManager coreGameManager = (CoreGameManager) gameManager;
            coreGameManager.initialize();
        });
        CommandRegister register = HyperNiteMC.getAPI().getCommandRegister();
        register.registerCommand(this, new MinigameCommand());
        this.getLogger().info("Minigames-Core enabled.");
    }

    public String getGamePrefix() {
        return Optional.ofNullable(arenaConfig.getGamePrefix()).orElse("");
    }

    @Override
    public FireWorkManager getFireWorkManager() {
        return fireWorkManager;
    }

    @Override
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    @Override
    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    @Override
    public GameItemManager getGameItemManager() {
        return gameItemManager;
    }

    @Override
    public MessageGetter getMessageGetter() {
        return messageGetter;
    }

    @Override
    public GameFactory getGameFactory() {
        return gameFactory;
    }

    @Override
    public ArenaCreateManager getArenaCreateManager() {
        return arenaCreateManager;
    }

    @Override
    public GameStatsManager getGameStatsManager() {
        return gameStatsManager;
    }

    public CoreInventoryManager getCoreInventoryManager() {
        return coreInventoryManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public GameUtils getGameUtils() {
        return gameUtils;
    }

    public GameTeam getGlobalTeam() {
        return voluntary.getGlobalTeam();
    }

    @Override
    public Compulsory getCompulsory() {
        return compulsory;
    }

    @Override
    public Voluntary getVoluntary() {
        return voluntary;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent e) {
        CoreArenaManager coreArenaManager = (CoreArenaManager) arenaManager;
        LangConfig lang = configManager.getConfigAs(LangConfig.class);
        boolean joinStart = configManager.getConfigAs(MGConfig.class).lunchGameOnStart;
        if (!coreArenaManager.isLoaded() && joinStart) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, lang.getPure("arena.kick-not-loaded"));
        } else if (gameManager.getGameState() == GameState.PREEND || gameManager.getGameState() == GameState.ENDED) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, lang.getPure("game-is-ending"));
        } else if (!((CoreGameManager) gameManager).isActivated()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, lang.getPure("game-not-loaded"));
        }
    }

    public static class MessageGetterImpl implements MessageGetter {

        private final LangConfig config;

        public MessageGetterImpl(LangConfig config) {
            this.config = config;
        }

        @Override
        public String getPrefix() {
            return config.getPrefix();
        }

        @Override
        public String get(String s) {
            return config.get(s);
        }

        @Override
        public String getPure(String s) {
            return config.getPure(s);
        }

        @Override
        public List<String> getList(String s) {
            return config.getList(s);
        }

        @Override
        public List<String> getPureList(String s) {
            return config.getPureList(s);
        }
    }
}

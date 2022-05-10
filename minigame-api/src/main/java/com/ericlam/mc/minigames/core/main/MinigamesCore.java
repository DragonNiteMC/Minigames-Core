package com.ericlam.mc.minigames.core.main;

import com.ericlam.mc.minigames.core.MinigamesAPI;
import com.ericlam.mc.minigames.core.Properties;
import com.ericlam.mc.minigames.core.factory.GameFactory;
import com.ericlam.mc.minigames.core.manager.*;
import com.ericlam.mc.minigames.core.registable.Compulsory;
import com.ericlam.mc.minigames.core.registable.Registration;
import com.ericlam.mc.minigames.core.registable.Voluntary;
import com.dragonite.mc.dnmc.core.config.MessageGetter;

import java.util.Optional;

/**
 * 所有 API 的集中獲取器
 */
public final class MinigamesCore implements MinigamesAPI, Registration, Properties {

    /**
     * 獲取 遊戲 API
     * <p>
     * 注意，當必要內容尚未註冊成功時，將可能無法成功獲取API
     * </p>
     *
     * @return 遊戲內 API
     */
    public static MinigamesAPI getApi() {
        throw new RuntimeException();
    }

    /**
     * 安全地獲取 API
     *
     * @return 遊戲內 API
     */
    public static Optional<MinigamesAPI> getApiSafe() {
        throw new RuntimeException();
    }

    /**
     * 獲取註冊管理器
     *
     * @return 註冊管理器
     */
    public static Registration getRegistration() {
        throw new RuntimeException();
    }

    /**
     * 獲取其他設定, 包括 Config 和 工廠等等
     *
     * @return 設定
     */
    public static Properties getProperties() {
        throw new RuntimeException();
    }


    @Override
    public MessageGetter getMessageGetter() {
        throw new RuntimeException();
    }

    @Override
    public GameFactory getGameFactory() {
        return null;
    }

    @Override
    public FireWorkManager getFireWorkManager() {
        return null;
    }

    @Override
    public ArenaManager getArenaManager() {
        return null;
    }

    @Override
    public LobbyManager getLobbyManager() {
        return null;
    }

    @Override
    public ScheduleManager getScheduleManager() {
        return null;
    }

    @Override
    public GameItemManager getGameItemManager() {
        return null;
    }

    @Override
    public ArenaCreateManager getArenaCreateManager() {
        return null;
    }

    @Override
    public GameStatsManager getGameStatsManager() {
        return null;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return null;
    }

    @Override
    public GameManager getGameManager() {
        return null;
    }

    @Override
    public GameUtils getGameUtils() {
        return null;
    }

    @Override
    public Compulsory getCompulsory() {
        return null;
    }

    @Override
    public Voluntary getVoluntary() {
        return null;
    }
}

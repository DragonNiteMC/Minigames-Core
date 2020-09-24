package com.ericlam.mc.minigames.core.manager;

import com.ericlam.mc.minigames.core.arena.Arena;
import com.ericlam.mc.minigames.core.exception.arena.ArenaNotLoadedException;
import com.ericlam.mc.minigames.core.exception.arena.FinalArenaAlreadyExistException;

import java.util.List;

/**
 * 場地管理器
 */
public interface ArenaManager {

    /**
     * 獲取投票最終結果場地，
     * 結果尚未出爐，則為 null
     *
     * @return 最終場地
     */
    Arena getFinalArena();

    /**
     * 獲取已被加載的場地
     *
     * @return 已被加載的場地
     * @throws ArenaNotLoadedException         場地未加載時
     * @throws FinalArenaAlreadyExistException 最終場地已經出現時
     */
    List<Arena> getLoadedArenas() throws ArenaNotLoadedException, FinalArenaAlreadyExistException;
}

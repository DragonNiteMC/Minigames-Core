package com.ericlam.mc.minigames.core.implement;

import com.ericlam.mc.minigames.core.game.GameTeam;
import org.bukkit.ChatColor;

public final class GlobalTeam implements GameTeam {
    @Override
    public String getTeamName() {
        return "Global";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    @Override
    public boolean isEnabledFriendlyFire() {
        return true;
    }
}

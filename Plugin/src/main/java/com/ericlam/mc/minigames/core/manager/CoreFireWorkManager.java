package com.ericlam.mc.minigames.core.manager;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CoreFireWorkManager implements FireWorkManager {


    @Override
    public FireworkMeta getFirework(Firework firework) {
        Random random = new Random();
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(random.nextInt(3));
        boolean flicker = random.nextBoolean();
        boolean trail = random.nextBoolean();
        List<Color> colors = new ArrayList<>();
        List<Color> fadeColors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Color color = getRandomColor(random);
            if (i > 2) colors.add(color);
            else fadeColors.add(color);
        }
        int index = random.nextInt(FireworkEffect.Type.values().length);
        FireworkEffect.Builder builder = FireworkEffect.builder().flicker(flicker).trail(trail).withColor(colors).withFade(fadeColors).with(FireworkEffect.Type.values()[index]);
        meta.addEffect(builder.build());
        return meta;
    }

    @Override
    public FireworkMeta getQuickFirework(Firework firework) {
        Random random = new Random();
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(random.nextInt(2));
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            colors.add(getRandomColor(random));
        }
        int index = random.nextInt(FireworkEffect.Type.values().length);
        FireworkEffect.Builder builder = FireworkEffect.builder().flicker(false).trail(false).withColor(colors).with(FireworkEffect.Type.values()[index]);
        meta.addEffect(builder.build());
        return meta;
    }

    @Override
    public Color getRandomColor(Random random) {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.fromRGB(red, green, blue);
    }

    @Override
    public void spawnFireWork(Player player) {
        Location loc = player.getLocation();
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        firework.setFireworkMeta(getFirework(firework));
    }

    @Override
    public void spawnFireWork(List<Location> locations) {
        for (Location location : locations) {
            Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            firework.setFireworkMeta(getQuickFirework(firework));
        }
    }
}

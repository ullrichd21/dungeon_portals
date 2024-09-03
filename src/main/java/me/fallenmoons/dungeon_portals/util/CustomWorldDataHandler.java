package me.fallenmoons.dungeon_portals.util;

import net.minecraft.server.level.ServerLevel;

public class CustomWorldDataHandler {

    public static void increaseCustomValue(ServerLevel level, int increment) {
        // Retrieve or create the custom world data
        CustomWorldData data = CustomWorldData.getOrCreate(level);

        // Modify the custom value
        int currentValue = data.getCustomValue();
        data.setCustomValue(currentValue + increment);

        System.out.println("Custom value increased to: " + data.getCustomValue());
    }

    public static int getCustomValue(ServerLevel level) {
        // Retrieve or create the custom world data
        CustomWorldData data = CustomWorldData.getOrCreate(level);

        // Return the current custom value
        return data.getCustomValue();
    }
}
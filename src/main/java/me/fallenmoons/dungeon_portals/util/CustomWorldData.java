package me.fallenmoons.dungeon_portals.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class CustomWorldData extends SavedData {
    private static final String DATA_NAME = "dungeon_portals_custom_data";

    private int customValue; // Example of custom data you want to store

    public CustomWorldData() {
        this.customValue = 0; // Initialize with default value
    }

    public int getCustomValue() {
        return customValue;
    }

    public void setCustomValue(int customValue) {
        this.customValue = customValue;
        setDirty(); // Mark data as dirty to ensure it's saved
    }

    // Method to save data to NBT
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("CustomValue", customValue);
        return compound;
    }

    // Method to load data from NBT
    public static CustomWorldData load(CompoundTag compound) {
        CustomWorldData data = new CustomWorldData();
        data.customValue = compound.getInt("CustomValue");
        return data;
    }

    // Static method to retrieve the data from the level
    public static CustomWorldData getOrCreate(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(CustomWorldData::load, CustomWorldData::new, DATA_NAME);
    }
}
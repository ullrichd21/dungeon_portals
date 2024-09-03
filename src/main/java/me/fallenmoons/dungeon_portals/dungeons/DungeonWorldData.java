package me.fallenmoons.dungeon_portals.dungeons;

import me.fallenmoons.dungeon_portals.dungeons.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class DungeonWorldData extends SavedData {
    private static final String DATA_NAME = "dungeon_world_data";

    private final Map<UUID, Dungeon> activeDungeons = new HashMap<>();
    private final Collection<BlockPos> historicalDungeons = new ArrayList<>();

    public DungeonWorldData() {}

    public Map<UUID, Dungeon> getActiveDungeons() {
        return activeDungeons;
    }

    public Collection<BlockPos> getHistoricalDungeons() {
        return historicalDungeons;
    }

    public void setDungeonData(Map<UUID, Dungeon> dungeons, Collection<BlockPos> historicalDungeons) {
        this.activeDungeons.clear();
        this.activeDungeons.putAll(dungeons);

        this.historicalDungeons.clear();
        this.historicalDungeons.addAll(historicalDungeons);

        setDirty(); // Mark data as dirty to ensure it gets saved
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        // Save active dungeons
        ListTag dungeonsTag = new ListTag();
        for (Dungeon dungeon : activeDungeons.values()) {
            CompoundTag dungeonTag = new CompoundTag();
            dungeon.save(dungeonTag);
            dungeonsTag.add(dungeonTag);
        }
        compound.put("ActiveDungeons", dungeonsTag);

        ListTag historicalDungeonsTag = new ListTag();
        for (BlockPos pos : historicalDungeons) {
            CompoundTag historicalDungeonTag = new CompoundTag();
            historicalDungeonTag.putLong("Pos", pos.asLong());
            historicalDungeonsTag.add(historicalDungeonTag);
        }
        compound.put("HistoricalDungeons", historicalDungeonsTag);

        return compound;
    }

    public static DungeonWorldData load(CompoundTag compound) {
        DungeonWorldData data = new DungeonWorldData();

        // Load active dungeons
        ListTag dungeonsTag = compound.getList("ActiveDungeons", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < dungeonsTag.size(); i++) {
            CompoundTag dungeonTag = dungeonsTag.getCompound(i);
            Dungeon dungeon = Dungeon.load(dungeonTag);
            data.activeDungeons.put(dungeon.getUUID(), dungeon);
        }

        // Load historical dungeons
        ListTag historicalDungeonsTag = compound.getList("HistoricalDungeons", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < historicalDungeonsTag.size(); i++) {
            CompoundTag historicalDungeonTag = historicalDungeonsTag.getCompound(i);
            BlockPos pos = BlockPos.of(historicalDungeonTag.getLong("Pos"));
            data.historicalDungeons.add(pos);
        }

        return data;
    }

    public static DungeonWorldData getOrCreate(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(DungeonWorldData::load, DungeonWorldData::new, DATA_NAME);
    }
}

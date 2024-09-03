package me.fallenmoons.dungeon_portals.dungeons;

import me.fallenmoons.dungeon_portals.dungeons.Dungeon;
import me.fallenmoons.dungeon_portals.dungeons.DungeonWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DungeonManager {
    private static DungeonManager instance;
    private final Map<UUID, Dungeon> activeDungeons;
    private final ArrayList<BlockPos> historicalDungeons;

    private DungeonManager() {
        this.activeDungeons = new HashMap<>();
        this.historicalDungeons = new ArrayList<>();
    }

    public static DungeonManager getInstance(ServerLevel level) {
        if (instance == null) {
            instance = new DungeonManager();
            instance.loadDataFromWorld(level);
        }
        return instance;
    }

    public Map<UUID, Dungeon> getActiveDungeons() {
        return activeDungeons;
    }

    public ArrayList<BlockPos> getHistoricalDungeons() {
        return historicalDungeons;
    }

    public void addActiveDungeon(Dungeon dungeon) {
        this.activeDungeons.put(dungeon.getUUID(), dungeon);
    }

    public void removeActiveDungeon(UUID dungeonUUID) {
        this.historicalDungeons.add(activeDungeons.get(dungeonUUID).getDungeonPos());
        System.out.println("Dungeon added to historical dungeons: " + activeDungeons.get(dungeonUUID).getDungeonPos());
        this.activeDungeons.remove(dungeonUUID);
    }

    public void addPlayerToDungeon(UUID dungeonUUID, Player player) {
        Dungeon dungeon = activeDungeons.get(dungeonUUID);
        if (dungeon != null) {
            dungeon.addPlayer(player.getUUID());
        }
    }

    public void removePlayerFromDungeon(UUID dungeonUUID, Player player) {
        Dungeon dungeon = activeDungeons.get(dungeonUUID);
        if (dungeon != null) {
            dungeon.removePlayer(player.getUUID());
        }
    }

    public void removeAllPlayersFromDungeon(UUID dungeonUUID) {
        Dungeon dungeon = activeDungeons.get(dungeonUUID);
        if (dungeon != null) {
            dungeon.removeAllPlayers();
        }
    }

    public Dungeon getDungeonLinkedToPortal(BlockPos pos, ServerLevel level) {
        for (Dungeon dungeon : activeDungeons.values()) {
            if (dungeon.getPortalBlockPos().equals(pos)) {
                return dungeon;
            }
        }
        return null;
    }

    public void saveDataToWorld(ServerLevel level) {
        DungeonWorldData data = DungeonWorldData.getOrCreate(level);

        // Store the data in DungeonWorldData
        data.setDungeonData(activeDungeons, historicalDungeons);
    }

    public void loadDataFromWorld(ServerLevel level) {
        DungeonWorldData data = DungeonWorldData.getOrCreate(level);

        // Load dungeons
        activeDungeons.clear();
        activeDungeons.putAll(data.getActiveDungeons());

        // Load historical dungeons
        historicalDungeons.clear();
        historicalDungeons.addAll(data.getHistoricalDungeons());
    }
}

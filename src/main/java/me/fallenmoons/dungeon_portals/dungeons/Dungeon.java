package me.fallenmoons.dungeon_portals.dungeons;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dungeon {
    private UUID uuid;
    private BlockPos spawnPos;
    private BlockPos dungeonPos;
    private BlockPos bossSpawnPos;
    private List<BlockPos> lootChests;
    private List<UUID> joinedPlayers; // Now stores active players directly

    private BlockPos portalBlockPos;

    public Dungeon() {
        // Default constructor
        this.uuid = UUID.randomUUID();
        this.spawnPos = BlockPos.ZERO;
        this.dungeonPos = BlockPos.ZERO;
        this.bossSpawnPos = BlockPos.ZERO;
        this.lootChests = new ArrayList<>();
        this.joinedPlayers = new ArrayList<>();
    }

    public Dungeon(BlockPos spawnPos, BlockPos bossSpawnPos, List<BlockPos> lootChests, BlockPos dungeonPos) {
        this.uuid = UUID.randomUUID(); // Generate a unique UUID for each dungeon
        this.spawnPos = spawnPos;
        this.dungeonPos = dungeonPos;
        this.bossSpawnPos = bossSpawnPos;
        this.lootChests = lootChests;
        this.joinedPlayers = new ArrayList<>();
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setDungeonPos(BlockPos dungeonPos) {
        this.dungeonPos = dungeonPos;
    }

    public BlockPos getDungeonPos() {
        return dungeonPos;
    }

    public BlockPos getSpawnPos() {
        return spawnPos;
    }

    public void setSpawnPos(BlockPos spawnPos) {
        this.spawnPos = spawnPos;
    }

    public BlockPos getBossSpawnPos() {
        return bossSpawnPos;
    }

    public void setBossSpawnPos(BlockPos bossSpawnPos) {
        this.bossSpawnPos = bossSpawnPos;
    }

    public List<BlockPos> getLootChests() {
        return lootChests;
    }

    public void setLootChests(List<BlockPos> lootChests) {
        this.lootChests = lootChests;
    }

    public List<UUID> getJoinedPlayers() {
        return joinedPlayers;
    }

    public void addPlayer(UUID playerUUID) {
        if (!joinedPlayers.contains(playerUUID)) {
            joinedPlayers.add(playerUUID);
        }
    }

    public void removePlayer(UUID playerUUID) {
        joinedPlayers.remove(playerUUID);
    }

    public BlockPos getPortalBlockPos() {
        return portalBlockPos;
    }

    public void setPortalBlockPos(BlockPos portalBlockPos) {
        this.portalBlockPos = portalBlockPos;
    }

    // Save dungeon data to NBT
    public void save(CompoundTag tag) {
        tag.putUUID("UUID", uuid);
        tag.putLong("SpawnPos", spawnPos.asLong());
        tag.putLong("BossSpawnPos", bossSpawnPos.asLong());

        ListTag lootChestList = new ListTag();
        for (BlockPos chestPos : lootChests) {
            lootChestList.add(LongTag.valueOf(chestPos.asLong()));
        }
        tag.put("LootChests", lootChestList);

        ListTag playersList = new ListTag();
        for (UUID playerUUID : joinedPlayers) {
            playersList.add(StringTag.valueOf(playerUUID.toString()));
        }
        tag.put("JoinedPlayers", playersList);

        tag.putLong("PortalBlockPos", portalBlockPos.asLong());
    }

    // Load dungeon data from NBT
    public static Dungeon load(CompoundTag tag) {
        Dungeon dungeon = new Dungeon();

        dungeon.uuid = tag.getUUID("UUID");
        dungeon.spawnPos = BlockPos.of(tag.getLong("SpawnPos"));
        dungeon.bossSpawnPos = BlockPos.of(tag.getLong("BossSpawnPos"));

        ListTag lootChestList = tag.getList("LootChests", LongTag.TAG_LONG);
        List<BlockPos> lootChests = new ArrayList<>();
        for (int i = 0; i < lootChestList.size(); i++) {
            LongTag longTag = (LongTag) lootChestList.get(i);
            lootChests.add(BlockPos.of(longTag.getAsLong()));
        }
        dungeon.lootChests = lootChests;

        ListTag playersList = tag.getList("JoinedPlayers", StringTag.TAG_STRING);
        List<UUID> players = new ArrayList<>();
        for (int i = 0; i < playersList.size(); i++) {
            players.add(UUID.fromString(playersList.getString(i)));
        }
        dungeon.joinedPlayers = players;

        dungeon.portalBlockPos = BlockPos.of(tag.getLong("PortalBlockPos"));

        return dungeon;
    }

    public String toString() {
        return "Dungeon: " + uuid.toString() + " at " + dungeonPos.toString() + " with " + joinedPlayers.size() + " players" + " and " + lootChests.size() + " loot chests" + " and portal at " + portalBlockPos.toString() + " and boss at " + bossSpawnPos.toString();
    }

    public void removeAllPlayers() {
        joinedPlayers.clear();
    }
}

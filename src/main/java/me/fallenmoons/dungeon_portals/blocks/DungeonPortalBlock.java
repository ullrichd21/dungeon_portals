package me.fallenmoons.dungeon_portals.blocks;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.blockentity.DungeonPortalBlockEntity;
import me.fallenmoons.dungeon_portals.dungeons.Dungeon;
import me.fallenmoons.dungeon_portals.dungeons.DungeonManager;
import me.fallenmoons.dungeon_portals.init.BlockEntityInit;
import me.fallenmoons.dungeon_portals.init.DimensionInit;
import me.fallenmoons.dungeon_portals.networking.ModNetworkHandler;
import me.fallenmoons.dungeon_portals.networking.ServerToClientMessage;
import me.fallenmoons.dungeon_portals.util.PlayerUtils;
import me.fallenmoons.dungeon_portals.util.VectorUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import me.fallenmoons.dungeon_portals.dungeons.DungeonGenerator;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static me.fallenmoons.dungeon_portals.dungeons.DungeonGenerator.generateDungeonStructure;


public class DungeonPortalBlock extends Block implements EntityBlock {
    public DungeonPortalBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DungeonPortalBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
//        System.out.println("Clicked.");
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
//            System.out.println("Not Clientside");
            if (blockEntity instanceof DungeonPortalBlockEntity) {
//                System.out.println("Is instance of DungeonPortalBlockEntity");
                DungeonPortalBlockEntity dungeonPortalBlockEntity = (DungeonPortalBlockEntity) blockEntity;
                if (!dungeonPortalBlockEntity.isInitialized()) {
                    System.out.println("Not initialized");
                    if (dungeonPortalBlockEntity.checkAltar()) {
                        System.out.println("Altar built correctly");
                        dungeonPortalBlockEntity.setInitialized(true);
                        dungeonPortalBlockEntity.setChanged();
                        pLevel.playSeededSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.0f,0);
                        // Define the target point (e.g., around the player's current position)
                        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(pPos.getX(), pPos.getY(), pPos.getZ(), 50.0D, pLevel.dimension());
//                        System.out.println("Sent packet");
                        System.out.println(pLevel.dimension().toString() + " \n " + pPos.toString());
                        for (Vec3 vec : dungeonPortalBlockEntity.getAltarBlockPositionsVec3()) {
                            ModNetworkHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), new ServerToClientMessage(vec.add(0.0, 0.8, 0.0), 0));
                        }


                        return InteractionResult.SUCCESS;
                    } else {
                        pLevel.playSeededSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f,0);
                        return InteractionResult.FAIL;
                    }
                }
            }

            DungeonPortalBlockEntity dungeonPortalBlockEntity = (DungeonPortalBlockEntity) blockEntity;

            if (dungeonPortalBlockEntity.checkAltar()) {
                System.out.println("Altar built correctly");
            } else {
                System.out.println("Altar not built correctly");
                pLevel.playSeededSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f,0);
                dungeonPortalBlockEntity.setInitialized(false);
                return InteractionResult.FAIL;
            }

            System.out.println("Initialized");

            for (Vec3 vec : dungeonPortalBlockEntity.getAltarBlockPositionsVec3()) {
                System.out.println("Checking position: " + vec);
                Player closestPlayer = PlayerUtils.getClosestPlayerStandingOnPos((ServerLevel) pLevel, new BlockPos(VectorUtils.toVec3i(vec)));
//                System.out.println("Closest player: " + closestPlayer.getName().getString());
                if (closestPlayer != null) {
                    if (closestPlayer.canChangeDimensions()) {
                        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(vec.x(), vec.y(), vec.z(), 50.0D, pLevel.dimension());
                        ModNetworkHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), new ServerToClientMessage(vec.add(0.0, 0.8, 0.0), 0));
                        handlePortal(pPos, (ServerLevel) pLevel, pPlayer);
                    } else {
                        System.out.println("Cannot change dimensions");
                    }
                }

            }
        }


        return InteractionResult.CONSUME;
    }

    /**
     * Generates a dungeon structure at a location far enough from existing dungeons
     * and registers it in the DungeonManager.
     *
     * @param portalPos The position of the portal.
     * @param level The world in which to generate the dungeon.
     * @return The newly created Dungeon.
     */
    private Dungeon generateAndRegisterDungeon(BlockPos portalPos, ServerLevel level) {
        // Create a new Dungeon object
        Dungeon newDungeon = new Dungeon();
        newDungeon.setPortalBlockPos(portalPos);

        // Find a suitable location for the dungeon, away from existing dungeons
        BlockPos dungeonPos = findSuitableDungeonLocation(level);
        System.out.println("Suitable dungeon location found at: " + dungeonPos);
        newDungeon.setDungeonPos(dungeonPos);

        // Register the dungeon in DungeonManager
        DungeonManager.getInstance(level).addActiveDungeon(newDungeon);

        // Create the dungeon structure
        generateDungeonStructure(dungeonPos, level, newDungeon);



        System.out.println("Dungeon generated and registered at: " + dungeonPos + " with spawn point at: " + newDungeon.getSpawnPos());

        DungeonManager.getInstance(level).saveDataToWorld(level);

        return newDungeon;
    }

    /**
     * Finds a suitable location for a new dungeon, ensuring it is far enough from existing dungeons.
     *
     * @param level The world to search in.
     * @return A suitable BlockPos for the new dungeon.
     */
    private BlockPos findSuitableDungeonLocation(ServerLevel level) {
        // Example: Start searching 500 blocks away from 0,0,0
        System.out.println("Finding suitable dungeon location");
        int distance = 500;
        BlockPos newDungeonPos = new BlockPos(0, level.getSeaLevel(), 0).offset(distance, 0, distance);

        // Ensure this position is far from any existing dungeon
        while (isDungeonTooClose(newDungeonPos, level)) {
            distance += 500; // Increment distance
            newDungeonPos = newDungeonPos.offset(distance, 0, distance);
        }

        return newDungeonPos;
    }

    /**
     * Checks if a position is too close to existing dungeons.
     *
     * @param pos The position to check.
     * @param level The world to check in.
     * @return True if the position is too close to any existing dungeon, false otherwise.
     */
    private boolean isDungeonTooClose(BlockPos pos, ServerLevel level) {
        System.out.println("Checking if dungeon is too close to existing dungeons");
        System.out.println(DungeonManager.getInstance(level).getActiveDungeons().values().toString());
        for (Dungeon dungeon : DungeonManager.getInstance(level).getActiveDungeons().values()) {
            System.out.println("Dungeon Pos: " + dungeon.getDungeonPos() + "> Proposed Pos: " + pos);
            if (dungeon.getDungeonPos().closerThan(pos, 500)) {
                return true;
            }
        }

        if (DungeonManager.getInstance(level).getHistoricalDungeons().isEmpty()) {
            return false;
        }

        for (BlockPos oldDungeonPos : DungeonManager.getInstance(level).getHistoricalDungeons()) {
            System.out.println("oldDungeon Pos: " + oldDungeonPos + "> Proposed Pos: " + pos);
            if (oldDungeonPos.closerThan(pos, 500)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Teleports the player to the dungeon's spawn position.
     *
     * @param player The player to teleport.
     * @param spawnPos The position to teleport the player to.
     */
    private void teleportPlayerToDungeon(Player player, BlockPos spawnPos) {
        Vec3 teleportPos = new Vec3(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
        ResourceKey<Level> resourceKey = DimensionInit.DUNGEON_LEVEL_KEY;
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
        player.teleportTo(player.getServer().getLevel(resourceKey), teleportPos.x, teleportPos.y, teleportPos.z, set, 0f, 0f);
        System.out.println("Player " + player.getName().getString() + " teleported to dungeon at: " + spawnPos);
    }

    /**
     * Retrieves the dungeon linked to a specific portal position, if any.
     *
     * @param pos The position of the portal.
     * @param level The world to check in.
     * @return The linked Dungeon, or null if none is linked.
     */
    private Dungeon getDungeonLinkedToPortal(BlockPos pos, ServerLevel level) {
        for (Dungeon dungeon : DungeonManager.getInstance(level).getActiveDungeons().values()) {
            System.out.println("ActiveDungeon: " + dungeon.getPortalBlockPos());
            if (dungeon.getPortalBlockPos().equals(pos)) {
                return dungeon;
            }
        }
        return null;
    }

    /**
     * Generates the dungeon structure at the given position.
     *
     * @param pos The position to generate the dungeon structure at.
     * @param level The level (world) in which the dungeon is being generated.
     * @return The position of the dungeon's spawn point.
     */
    private void generateDungeonStructure(BlockPos pos, ServerLevel level, Dungeon newDungeon) {
        // Example of generating a simple dungeon structure
        ResourceKey<Level> resourceKey = DimensionInit.DUNGEON_LEVEL_KEY;
        DungeonGenerator.generateDungeonStructure(level.getServer().getLevel(resourceKey), pos, newDungeon);
    }

    public void handlePortal(BlockPos pos, ServerLevel level, Player player) {
        System.out.println("Handling portal");

        // Check if a dungeon is already linked to this portal
        Dungeon linkedDungeon = getDungeonLinkedToPortal(pos, level);

        if (linkedDungeon == null) {
            // Generate a new dungeon if none is linked
            linkedDungeon = generateAndRegisterDungeon(pos, level);
        }

        //Make sure the player is added to the dungeon when teleporting.
        linkedDungeon.addPlayer(player.getUUID());

        // Teleport the player to the dungeon's spawn point
        teleportPlayerToDungeon(player, linkedDungeon.getSpawnPos());
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);

        if (!pLevel.isClientSide()) {
            // Check if the broken block is a Dungeon Portal Block
            System.out.println(pState.getBlock().toString());
            if (pState.getBlock() instanceof DungeonPortalBlock) {
                // Find the linked dungeon and remove it from the DungeonManager
                System.out.println("Block is DungeonPortalBlock");
                DungeonManager dungeonManager = DungeonManager.getInstance((ServerLevel) pLevel);
                Dungeon linkedDungeon = dungeonManager.getDungeonLinkedToPortal(pPos, (ServerLevel) pLevel);

                System.out.println("Linked Dungeon: " + linkedDungeon);

                if (linkedDungeon != null) {
                    dungeonManager.removeActiveDungeon(linkedDungeon.getUUID());
                    System.out.println("Dungeon linked to portal at " + pPos + " has been removed from the manager.");
                }
            }
        }
    }

    //    private void handlePortal(Entity player, BlockPos pPos) {
//        if (player.level() instanceof ServerLevel) {
//            if (!player.level().isClientSide()) {
//                MinecraftServer server = player.getServer();
//                ResourceKey<Level> resourceKey = DimensionInit.DUNGEON_LEVEL_KEY;
//                Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
////                player.changeDimension();
//                if (Level.isInSpawnableBounds(pPos)) {
//                    Dungeon newDungeon = new Dungeon();
//                    generateDungeonStructure(player.getServer().getLevel(resourceKey), pPos, newDungeon);
//                    newDungeon.setPortalBlockPos(pPos);
//                    DungeonManager dungeonManager = DungeonManager.getInstance();
//                    dungeonManager.addActiveDungeon(newDungeon);
//
//                    player.teleportTo(
//                            player.getServer().getLevel(resourceKey), newDungeon.getSpawnPos().getX(), newDungeon.getSpawnPos().getY(), newDungeon.getSpawnPos().getZ(), set, 0f, 0f
//                    );
//                    player.setOnGround(true);
//                }
//            }
//        }
//    }
}

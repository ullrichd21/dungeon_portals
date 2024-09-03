package me.fallenmoons.dungeon_portals.blockentity;

import me.fallenmoons.dungeon_portals.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

import static net.minecraft.commands.arguments.blocks.BlockStateArgument.getBlock;

public class DungeonPortalBlockEntity extends BlockEntity {

    private boolean isInitialized = false;
    private ArrayList<UUID> playerUUIDs = new ArrayList<UUID>();

    private final Vec3i[] altarBlocks = new Vec3i[]{
            new Vec3i(3,-1, 0),
            new Vec3i(0,-1, 3),
            new Vec3i(-3,-1, 0),
            new Vec3i(0,-1, -3),
            new Vec3i(2,-1, 2),
            new Vec3i(2,-1, -2),
            new Vec3i(-2,-1, -2),
            new Vec3i(-2,-1, 2)
    };

    public DungeonPortalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DUNGEON_PORTAL_BLOCK_ENTITY.get(), pos, state);
        System.out.println("Block entity created");
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
        setChanged();
    }

    public boolean checkAltar() {
        // checkAltar, returns true if altar is built correctly
        BlockPos altarPos = getBlockPos();


        for (Vec3i vec : altarBlocks) {
            if (level.getBlockState(altarPos.offset(vec)).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<BlockPos> getAltarBlockPositions() {
        BlockPos altarPos = getBlockPos();

        ArrayList<BlockPos> altarBlocksList = new ArrayList<BlockPos>();

        for (Vec3i vec : altarBlocks) {
            altarBlocksList.add(altarPos.offset(vec));
        }

        return altarBlocksList;
    }

    public ArrayList<Vec3> getAltarBlockPositionsVec3() {
        BlockPos altarPos = getBlockPos();

        ArrayList<Vec3> altarBlocksList = new ArrayList<Vec3>();

        for (Vec3i vec : altarBlocks) {
            Vec3 vv = new Vec3(vec.getX(), vec.getY(), vec.getZ());
            Vec3 v = altarPos.getCenter().add(vv);
            altarBlocksList.add(v);
        }

        return altarBlocksList;
    }

    public void addPlayer(UUID uuid) {
        playerUUIDs.add(uuid);
        setChanged();
    };

    public void removePlayer(UUID uuid) {
        playerUUIDs.remove(uuid);
        setChanged();
    };

    public void clearPlayers() {
        playerUUIDs.clear();
        setChanged();
    };




    // Method to save data to NBT
    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isInitialized", isInitialized);

        ListTag playerListTag = new ListTag();
        for (UUID uuid : playerUUIDs) {
            playerListTag.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put("PlayerUUIDs", playerListTag);
    }

    // Method to load data from NBT
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isInitialized = tag.getBoolean("isInitialized");
    }
}

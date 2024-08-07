package me.fallenmoons.dungeon_portals.blockentity;

import me.fallenmoons.dungeon_portals.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonPortalBlockEntity extends BlockEntity {
    public DungeonPortalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DUNGEON_PORTAL_BLOCK_ENTITY.get(), pos, state);
    }
}

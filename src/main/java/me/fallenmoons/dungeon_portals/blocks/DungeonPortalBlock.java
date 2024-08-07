package me.fallenmoons.dungeon_portals.blocks;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.init.BlockEntityInit;
import me.fallenmoons.dungeon_portals.init.DimensionInit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.common.util.ITeleporter;
import me.fallenmoons.dungeon_portals.dungeons.DungeonGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static me.fallenmoons.dungeon_portals.dungeons.DungeonGenerator.generateDungeonStructure;


public class DungeonPortalBlock extends Block implements EntityBlock {
    public DungeonPortalBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityInit.DUNGEON_PORTAL_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.canChangeDimensions()) {
            handlePortal(pPlayer, pPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    private void handlePortal(Entity player, BlockPos pPos) {
        if (player.level() instanceof ServerLevel) {
            if (!player.level().isClientSide()) {
                MinecraftServer server = player.getServer();
                ResourceKey<Level> resourceKey = DimensionInit.DUNGEON_LEVEL_KEY;
                Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
//                player.changeDimension();
                if (Level.isInSpawnableBounds(pPos)) {
                    generateDungeonStructure(player.getServer().getLevel(resourceKey), pPos);
                    player.teleportTo(
                            player.getServer().getLevel(resourceKey), pPos.getX(), pPos.getY(), pPos.getZ(), set, 0f, 0f
                    );
                    player.setOnGround(true);
                }
            }
        }
    }
}

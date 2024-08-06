package me.fallenmoons.dungeon_portals.blocks;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.init.DimensionInit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.ITeleporter;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;


public class DungeonPortalBlock extends Block {
    public DungeonPortalBlock(Properties properties) {
        super(properties);
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
                Dungeon_portals.LOGGER.debug(resourceKey.toString());
                Dungeon_portals.LOGGER.debug(Level.NETHER.toString());
                Dungeon_portals.LOGGER.debug(server.getLevel(Level.NETHER).toString());
                Dungeon_portals.LOGGER.debug(server.getLevel(resourceKey).toString());
                Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
//                player.changeDimension();
                if (Level.isInSpawnableBounds(pPos)) {
                    player.teleportTo(
                            player.getServer().getLevel(resourceKey), pPos.getX(), pPos.getY(), pPos.getZ(), set, 0f, 0f
                    );
                    player.setOnGround(true);
                }
            }
        }
    }
}

package me.fallenmoons.dungeon_portals.dungeons;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.blocks.DungeonPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dungeon_portals.MODID, value = Dist.DEDICATED_SERVER)
public class DungeonEventListener {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent event) {
        System.out.println("Block broken");

        // Ensure we're on the server side
        if (!(event.getLevel() instanceof ServerLevel)) {
            return;
        }

        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();

        // Check if the broken block is a Dungeon Portal Block
        if (block instanceof DungeonPortalBlock) {
            // Find the linked dungeon and remove it from the DungeonManager
            DungeonManager dungeonManager = DungeonManager.getInstance(level);
            Dungeon linkedDungeon = dungeonManager.getDungeonLinkedToPortal(pos, level);

            if (linkedDungeon != null) {
                dungeonManager.removeActiveDungeon(linkedDungeon.getUUID());
                System.out.println("Dungeon linked to portal at " + pos + " has been removed from the manager.");
            }
        }
    }
}

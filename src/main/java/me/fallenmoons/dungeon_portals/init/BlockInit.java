package me.fallenmoons.dungeon_portals.init;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.blocks.DungeonPortalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    // Create a Deferred Register to hold Blocks which will all be registered under the "dungeon_portals" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dungeon_portals.MODID);

    public static final RegistryObject<DungeonPortalBlock> DUNGEON_PORTAL_BLOCK = BLOCKS.register("dungeon_portal",
            () -> new DungeonPortalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> DUNGEON_SPAWN_BLOCK = BLOCKS.register("dungeon_spawn_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> DUNGEON_BOSS_SPAWN_BLOCK = BLOCKS.register("dungeon_boss_spawn_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> DUNGEON_LOOT_BLOCK = BLOCKS.register("dungeon_loot_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
}

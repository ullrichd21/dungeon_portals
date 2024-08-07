package me.fallenmoons.dungeon_portals.init;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.blockentity.DungeonPortalBlockEntity;
import me.fallenmoons.dungeon_portals.blocks.DungeonPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dungeon_portals.MODID);

    public static final RegistryObject<BlockEntityType<DungeonPortalBlockEntity>> DUNGEON_PORTAL_BLOCK_ENTITY = BLOCK_ENTITIES.register("dungeon_portal_block",
            () -> BlockEntityType.Builder.of(DungeonPortalBlockEntity::new, BlockInit.DUNGEON_PORTAL_BLOCK.get()).build(null));
}

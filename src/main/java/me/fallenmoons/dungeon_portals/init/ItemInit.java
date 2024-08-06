package me.fallenmoons.dungeon_portals.init;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    // Create a Deferred Register to hold Blocks which will all be registered under the "dungeon_portals" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dungeon_portals.MODID);

//    public static final RegistryObject<Block> DUNGEON_PORTAL = BLOCKS.register("dungeon_portal",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<BlockItem> DUNGEON_PORTAL_ITEM = CreativeTabInit.addToTab(ITEMS.register("dungeon_portal",
        () -> new BlockItem(BlockInit.DUNGEON_PORTAL.get(),
                new Item.Properties().rarity(Rarity.EPIC))));

}

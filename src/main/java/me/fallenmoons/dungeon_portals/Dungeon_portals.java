package me.fallenmoons.dungeon_portals;

import com.mojang.logging.LogUtils;
import me.fallenmoons.dungeon_portals.commands.ReturnPlayersCommand;
import me.fallenmoons.dungeon_portals.dungeons.DungeonEventListener;
import me.fallenmoons.dungeon_portals.init.*;
import me.fallenmoons.dungeon_portals.networking.ModNetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;


import static me.fallenmoons.dungeon_portals.init.BlockInit.BLOCKS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Dungeon_portals.MODID)
public class Dungeon_portals {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "dungeon_portals";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Level> DUNGEON_DIMENSION = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "dungeon"));

    public Dungeon_portals() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        ItemInit.ITEMS.register(bus);
        CreativeTabInit.TABS.register(bus);
        DimensionInit.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModNetworkHandler.registerMessages();
    }
}


/*
Lazy to do list:

TODO: Add a way to automatically select which dungeon to generate.
TODO: Add crafting recipes for the dungeon portal block.
TODO: Add a way to teleport multiple players to the dungeon.
TODO: Add a way to complete the dungeon.
TODO: Add boss mobs to the dungeon.
TODO: Add loot to the dungeon.
TODO: Add regular mobs to the dungeon.
TODO: Add dungeon resources to the dungeon.
TODO: Confirm saving is working.
TODO: Add a way to leave the dungeon (Command?).

 */
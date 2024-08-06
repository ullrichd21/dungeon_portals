package me.fallenmoons.dungeon_portals.init;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DimensionInit {
    public static final ResourceKey<Level> DUNGEON_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(Dungeon_portals.MODID, "dungeon"));
    public static final ResourceKey<DimensionType> DUNGEON_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(Dungeon_portals.MODID, "dungeon"));

    public static void init() {
        //oop.
    }
//    public static final DeferredRegister<DimensionType> DIMS = DeferredRegister.create(Registries.DIMENSION_TYPE, Dungeon_portals.MODID);
////
//    public static final RegistryObject<DimensionType> DUNGEON_DIM = RegistryObject.create(new ResourceLocation(Dungeon_portals.MODID, "dungeon"),DIMS.getRegistryKey());
}

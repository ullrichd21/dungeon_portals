package me.fallenmoons.dungeon_portals.dungeons;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import me.fallenmoons.dungeon_portals.init.BlockInit;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Predicate;

public class DungeonGenerator {
    public static void generateDungeonStructure(ServerLevel level, BlockPos pos, Dungeon dungeon) {

        boolean success = generate(level,64, pos, false, dungeon);

        if (!success) {
            Dungeon_portals.LOGGER.debug("Failed to gen dungeon");
        }
    }

    private static boolean generate(ServerLevel level, int size, BlockPos pos, boolean keepJigsaws, Dungeon dungeon) {

        HolderGetter<StructureTemplatePool> poolHolderGetter = level.holderLookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> poolHolder = poolHolderGetter.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(Dungeon_portals.MODID, "dark_dungeon/spawn")));
        ResourceLocation recLoc = new ResourceLocation(Dungeon_portals.MODID, "start");
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        StructureTemplateManager structureTemplateManager = level.getStructureManager();
        StructureManager structureAccessor = level.structureManager();
        RandomSource random = level.getRandom();
        Structure.GenerationContext context = new Structure.GenerationContext(level.registryAccess(), chunkGenerator, chunkGenerator.getBiomeSource(), level.getChunkSource().randomState(),
                structureTemplateManager, level.getSeed(), new ChunkPos(pos), level, registryEntry -> true);
        Optional<Structure.GenerationStub> optional = JigsawPlacement.addPieces(context, poolHolder, Optional.of(recLoc), size, pos, false, Optional.empty(), 512);
        if (optional.isPresent()) {
//            HashMap<Integer, ArrayList<BlockPos>> blockIdPosMap = new HashMap<Integer, ArrayList<BlockPos>>();
//            ArrayList<BlockPos> chestPosList = new ArrayList<BlockPos>();
//            ArrayList<BlockPos> exitPosList = new ArrayList<BlockPos>();
//            ArrayList<BlockPos> gatePosList = new ArrayList<BlockPos>();
//            HashMap<BlockPos, Integer> spawnerPosEntityIdMap = new HashMap<BlockPos, Integer>();
//            Block exitBlock = Registries.BLOCK.get(dungeon.getExitBlockId());
//            Block bossLootBlock = Registries.BLOCK.get(dungeon.getBossLootBlockId());

            StructurePiecesBuilder structurePiecesBuilder = optional.get().getPiecesBuilder();
            for (StructurePiece structurePiece : structurePiecesBuilder.build().pieces()) {
                if (!(structurePiece instanceof StructurePiece)) {
                    continue;
                }
                PoolElementStructurePiece poolStructurePiece = (PoolElementStructurePiece) structurePiece;
                poolStructurePiece.place((WorldGenLevel) level, structureAccessor, chunkGenerator, random, BoundingBox.infinite(), pos, keepJigsaws);

                //Loop to find special blocks.
                for (int i = poolStructurePiece.getBoundingBox().minX(); i <= poolStructurePiece.getBoundingBox().maxX(); i++) {
                    for (int j = poolStructurePiece.getBoundingBox().minY(); j <= poolStructurePiece.getBoundingBox().maxY(); j++) {
                        for (int k = poolStructurePiece.getBoundingBox().minZ(); k <= poolStructurePiece.getBoundingBox().maxZ(); k++) {
                            BlockPos checkPos = new BlockPos(i, j, k);
                            BlockState state = level.getBlockState(checkPos);
                            if (!state.isAir()) {
//                                Block block = state.getBlock();
//                                Dungeon_portals.LOGGER.error(state.toString());
                                if (state.is(BlockInit.DUNGEON_SPAWN_BLOCK.get())) {
                                    dungeon.setSpawnPos(checkPos);
                                    level.setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                                } else if (state.is(Blocks.DIAMOND_BLOCK)) {
                                    if (dungeon.getSpawnPos() == null || dungeon.getSpawnPos().equals(BlockPos.ZERO)) {
                                        dungeon.setSpawnPos(checkPos);
                                        System.out.println("Spawn pos set to: " + checkPos.above());
//                                        level.setBlock(checkPos, Blocks.AIR.defaultBlockState(), 3);
                                    }
                                }
//                                if (state.isOf(BlockInit.DUNGEON_SPAWNER)) {
//                                    spawnerPosEntityIdMap.put(checkPos, ((DungeonSpawnerEntity) world.getBlockEntity(checkPos)).getLogic().getEntityId());
//                                }
                            }

                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

//    private static void generate(ServerLevel level, Holder<StructureTemplatePool> structurePool, ResourceLocation resLoc, int size, BlockPos pos, boolean keepJigsaws) {
//        JigsawPlacement.addPieces(context, structurePool, opResLoc, size, pos, keepJigsaws, Optional.empty(), size);
//        StructurePoolBasedGenerator
//    }
}

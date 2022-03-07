package com.example.tutorialv3.worldgen.structures;

import com.example.tutorialv3.worldgen.dimensions.MysteriousChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PortalStructure extends StructureFeature<JigsawConfiguration> {

    public PortalStructure() {
        super(JigsawConfiguration.CODEC, context -> createPiecesGenerator(context), PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        boolean overworld = !(context.chunkGenerator() instanceof MysteriousChunkGenerator);

        // Turns the chunk coordinates into actual coordinates we can use. (center of that chunk)
        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        if (overworld) {
            // If we are generating for the overworld we want our portal to spawn underground. Preferably in an open area
            blockpos = findSuitableSpot(context, blockpos);
        }

        // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
        return JigsawPlacement.addPieces(
                context,
                PoolElementStructurePiece::new,
                blockpos,
                false,
                !overworld
        );
    }

    @NotNull
    private static BlockPos findSuitableSpot(PieceGeneratorSupplier.Context<JigsawConfiguration> context, BlockPos blockpos) {
        LevelHeightAccessor heightAccessor = context.heightAccessor();

        // Get the top y location that is solid
        int y = context.chunkGenerator().getBaseHeight(blockpos.getX(), blockpos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);

        // Create a randomgenerator that depends on the current chunk location. That way if the world is recreated
        // with the same seed the feature will end up at the same spot
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(context.seed()));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);

        // Pick a random y location between a low and a high point
        y = worldgenrandom.nextIntBetweenInclusive(heightAccessor.getMinBuildHeight()+20, y - 10);

        // Go down until we find a spot that has air. Then go down until we find a spot that is solid again
        NoiseColumn baseColumn = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), heightAccessor);
        int yy = y; // Remember 'y' because we will just use this if we can't find an air bubble
        int lower = heightAccessor.getMinBuildHeight() + 3; // Lower limit, don't go below this
        while (yy > lower && !baseColumn.getBlock(yy).isAir()) {
            yy--;
        }
        // If we found air we go down until we find a non-air block
        if (yy > lower) {
            while (yy > lower && baseColumn.getBlock(yy).isAir()) {
                yy--;
            }
            if (yy > lower) {
                // We found a possible spawn spot
                y = yy + 1;
            }
        }

        return blockpos.atY(y);
    }

}

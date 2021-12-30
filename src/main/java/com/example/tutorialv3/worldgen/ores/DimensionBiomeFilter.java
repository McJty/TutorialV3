package com.example.tutorialv3.worldgen.ores;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;
import java.util.function.Predicate;

/**
 * A biome filter that also checks if the dimension is right
 */
public class DimensionBiomeFilter extends PlacementFilter {

    private final Predicate<ResourceKey<Level>> levelTest;

    public DimensionBiomeFilter(Predicate<ResourceKey<Level>> levelTest) {
        this.levelTest = levelTest;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, Random random, BlockPos pos) {
        if (levelTest.test(context.getLevel().getLevel().dimension())) {
            PlacedFeature placedfeature = context.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature"));
            Biome biome = context.getLevel().getBiome(pos);
            return biome.getGenerationSettings().hasFeature(placedfeature);
        } else {
            return false;
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.BIOME_FILTER;
    }
}

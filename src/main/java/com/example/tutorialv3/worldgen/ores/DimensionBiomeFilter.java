package com.example.tutorialv3.worldgen.ores;

import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/**
 * A biome filter that also checks if the dimension is right
 */
public class DimensionBiomeFilter extends PlacementFilter {

    private final Predicate<ResourceKey<Level>> levelTest;

    public DimensionBiomeFilter(Predicate<ResourceKey<Level>> levelTest) {
        this.levelTest = levelTest;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        if (levelTest.test(context.getLevel().getLevel().dimension())) {
            PlacedFeature placedfeature = context.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature"));
            Holder<Biome> biome = context.getLevel().getBiome(pos);
            return biome.value().getGenerationSettings().hasFeature(placedfeature);
        } else {
            return false;
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.BIOME_FILTER;
    }
}

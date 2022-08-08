package com.example.tutorialv3.worldgen.ores;

import com.example.tutorialv3.setup.Registration;
import com.example.tutorialv3.worldgen.dimensions.Dimensions;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Ores {

    @NotNull
    public static PlacedFeature createOverworldOregen() {
        OreConfiguration config = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, Registration.MYSTERIOUS_ORE_OVERWORLD.get().defaultBlockState(), 5);
        return createPlacedFeature(new ConfiguredFeature<>(Feature.ORE, config),
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                new DimensionBiomeFilter(key -> !Dimensions.MYSTERIOUS.equals(key)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(90)));
    }

    @NotNull
    public static PlacedFeature createMysteriousOregen() {
        OreConfiguration config = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, Registration.MYSTERIOUS_ORE_OVERWORLD.get().defaultBlockState(), 25);
        return createPlacedFeature(new ConfiguredFeature<>(Feature.ORE, config),
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                new DimensionBiomeFilter(key -> key.equals(Dimensions.MYSTERIOUS)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(90)));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> PlacedFeature createPlacedFeature(ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        return new PlacedFeature(Holder.hackyErase(Holder.direct(feature)), List.copyOf(List.of(placementModifiers)));
    }

}

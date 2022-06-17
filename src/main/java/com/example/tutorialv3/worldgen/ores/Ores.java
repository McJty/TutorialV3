package com.example.tutorialv3.worldgen.ores;

import com.example.tutorialv3.setup.Registration;
import com.example.tutorialv3.worldgen.dimensions.Dimensions;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public class Ores {

    @NotNull
    public static Holder<PlacedFeature> createOverworldOregen() {
        OreConfiguration config = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, Registration.MYSTERIOUS_ORE_OVERWORLD.get().defaultBlockState(), OresConfig.OVERWORLD_VEINSIZE.get());
        return registerPlacedFeature("overworld_mysterious_ore", new ConfiguredFeature<>(Feature.ORE, config),
                CountPlacement.of(OresConfig.OVERWORLD_AMOUNT.get()),
                InSquarePlacement.spread(),
                new DimensionBiomeFilter(key -> !Dimensions.MYSTERIOUS.equals(key)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(90)));
    }

    @NotNull
    public static Holder<PlacedFeature> createMysteriousOregen() {
        OreConfiguration config = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, Registration.MYSTERIOUS_ORE_OVERWORLD.get().defaultBlockState(), OresConfig.MYSTERIOUS_VEINSIZE.get());
        return registerPlacedFeature("mysterious_mysterious_ore", new ConfiguredFeature<>(Feature.ORE, config),
                CountPlacement.of(OresConfig.MYSTERIOUS_AMOUNT.get()),
                InSquarePlacement.spread(),
                new DimensionBiomeFilter(key -> key.equals(Dimensions.MYSTERIOUS)),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(90)));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> Holder<PlacedFeature> registerPlacedFeature(String registryName, ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(registryName, Holder.direct(feature), placementModifiers);
    }
}

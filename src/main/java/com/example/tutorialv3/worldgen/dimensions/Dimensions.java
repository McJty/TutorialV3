package com.example.tutorialv3.worldgen.dimensions;

import com.example.tutorialv3.TutorialV3;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class Dimensions {

    public static final ResourceKey<Level> MYSTERIOUS = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TutorialV3.MODID, "mysterious"));

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(TutorialV3.MODID, "mysterious_chunkgen"),
                MysteriousChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(TutorialV3.MODID, "biomes"),
                MysteriousBiomeProvider.CODEC);
    }
}

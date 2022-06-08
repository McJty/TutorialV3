package com.example.tutorialv3.worldgen.dimensions;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MysteriousBiomeProvider extends BiomeSource {

    public static final Codec<MysteriousBiomeProvider> CODEC = RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY)
            .xmap(MysteriousBiomeProvider::new, MysteriousBiomeProvider::getBiomeRegistry).codec();

    private final Holder<Biome> biome;
    private final Registry<Biome> biomeRegistry;
    private static final List<ResourceKey<Biome>> SPAWN = Collections.singletonList(Biomes.PLAINS);

    public MysteriousBiomeProvider(Registry<Biome> biomeRegistry) {
        super(getStartBiomes(biomeRegistry));
        this.biomeRegistry = biomeRegistry;
        biome = biomeRegistry.getHolderOrThrow(Biomes.PLAINS);
    }

    private static List<Holder<Biome>> getStartBiomes(Registry<Biome> registry) {
        return SPAWN.stream().map(s -> registry.getHolderOrThrow(ResourceKey.create(BuiltinRegistries.BIOME.key(), s.location()))).collect(Collectors.toList());
    }

    public Registry<Biome> getBiomeRegistry() {
        return biomeRegistry;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

//    @Override
//    public BiomeSource withSeed(long seed) {
//        return this;
//    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return biome;
    }
}

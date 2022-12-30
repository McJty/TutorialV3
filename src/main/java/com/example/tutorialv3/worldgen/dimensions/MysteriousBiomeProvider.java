package com.example.tutorialv3.worldgen.dimensions;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
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

    public static final Codec<MysteriousBiomeProvider> CODEC = RegistryOps.retrieveRegistryLookup(Registries.BIOME)
            .xmap(MysteriousBiomeProvider::new, MysteriousBiomeProvider::getBiomeRegistry).codec();

    private final Holder<Biome> biome;
    private final HolderLookup.RegistryLookup<Biome> biomeRegistry;
    private static final List<ResourceKey<Biome>> SPAWN = Collections.singletonList(Biomes.PLAINS);

    public MysteriousBiomeProvider(HolderLookup.RegistryLookup<Biome> biomeRegistry) {
        super(getStartBiomes(biomeRegistry));
        this.biomeRegistry = biomeRegistry;
        biome = biomeRegistry.getOrThrow(Biomes.PLAINS);
    }

    private static List<Holder<Biome>> getStartBiomes(HolderLookup.RegistryLookup<Biome> registry) {
        return SPAWN.stream().map(s -> registry.getOrThrow(ResourceKey.create(Registries.BIOME, s.location()))).collect(Collectors.toList());
    }

    public HolderLookup.RegistryLookup<Biome> getBiomeRegistry() {
        return biomeRegistry;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return biome;
    }
}

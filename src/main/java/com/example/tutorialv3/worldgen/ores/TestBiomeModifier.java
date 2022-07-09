package com.example.tutorialv3.worldgen.ores;

import static com.example.tutorialv3.TutorialV3.MODID;

import com.example.tutorialv3.setup.Registration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record TestBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {

    public static final String TEST_BIOME_MODIFIER_NAME = "test_biome_modifier";
    public static final ResourceLocation TEST_BIOME_MODIFIER = new ResourceLocation(MODID, TEST_BIOME_MODIFIER_NAME);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome)) {
            builder.getEffects().ambientParticle(new AmbientParticleSettings(new SculkChargeParticleOptions(1.0f), 1.0f));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return Registration.TEST_BIOME_MODIFIER.get();
    }

    public static Codec<TestBiomeModifier> makeCodec() {
        return RecordCodecBuilder.create(builder -> builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(TestBiomeModifier::biomes)
        ).apply(builder, TestBiomeModifier::new));
    }
}


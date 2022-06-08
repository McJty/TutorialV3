package com.example.tutorialv3.worldgen.ores;

import com.example.tutorialv3.setup.Registration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import static com.example.tutorialv3.TutorialV3.MODID;

public record OreBiomeModifier(HolderSet<Biome> biomes, GenerationStep.Decoration generationStage,
                               HolderSet<PlacedFeature> features) implements BiomeModifier {

    public static final String ORE_BIOME_MODIFIER_NAME = "ore_biome_modifier";
    public static final ResourceLocation ORE_BIOME_MODIFIER = new ResourceLocation(MODID, ORE_BIOME_MODIFIER_NAME);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome)) {
            BiomeGenerationSettingsBuilder generation = builder.getGenerationSettings();
            this.features.forEach(holder -> generation.addFeature(this.generationStage, holder));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return Registration.ORE_BIOME_MODIFIER.get();
    }

    public static Codec<OreBiomeModifier> makeCodec() {
        return RecordCodecBuilder.create(builder -> builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(OreBiomeModifier::biomes),
                Codec.STRING.comapFlatMap(OreBiomeModifier::generationStageFromString, GenerationStep.Decoration::toString).fieldOf("generation_stage").forGetter(OreBiomeModifier::generationStage),
                PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(OreBiomeModifier::features)
        ).apply(builder, OreBiomeModifier::new));
    }

    private static DataResult<GenerationStep.Decoration> generationStageFromString(String name) {
        try {
            return DataResult.success(GenerationStep.Decoration.valueOf(name));
        } catch (Exception e) {
            return DataResult.error("Not a decoration stage: " + name);
        }
    }
}


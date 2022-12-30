package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class TutBiomeTags extends TagsProvider<Biome> {

    public TutBiomeTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(packOutput, Registries.BIOME, lookupProvider, TutorialV3.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        ForgeRegistries.BIOMES.getValues().forEach(biome -> {
            tag(Registration.HAS_ORE).add(TagEntry.tag(BiomeTags.IS_OVERWORLD.location()));
            tag(Registration.HAS_ORE).add(TagEntry.tag(BiomeTags.IS_NETHER.location()));
            tag(Registration.HAS_ORE).add(TagEntry.tag(BiomeTags.IS_END.location()));
            tag(Registration.HAS_PORTAL).add(ForgeRegistries.BIOMES.getResourceKey(biome).get());
            tag(Registration.HAS_THIEFDEN).add(ForgeRegistries.BIOMES.getResourceKey(biome).get());
        });
    }

    @Override
    public String getName() {
        return "Tutorial Biome Tags";
    }
}

package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class TutBiomeTags extends TagsProvider<Biome> {

    public TutBiomeTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, BuiltinRegistries.BIOME, TutorialV3.MODID, helper);
    }

    @Override
    protected void addTags() {
        ForgeRegistries.BIOMES.getValues().forEach(biome -> {
            tag(Registration.HAS_ORE).add(TagEntry.tag(BiomeTags.IS_OVERWORLD.location()));
            tag(Registration.HAS_ORE).add(TagEntry.tag(BiomeTags.IS_NETHER.location()));
            tag(Registration.HAS_ORE).add(TagEntry.tag(BiomeTags.IS_END.location()));
            tag(Registration.HAS_PORTAL).add(biome);
            tag(Registration.HAS_THIEFDEN).add(biome);
        });
    }

    @Override
    public String getName() {
        return "Tutorial Tags";
    }
}

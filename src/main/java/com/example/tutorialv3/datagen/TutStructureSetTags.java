package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagManager;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TutStructureSetTags extends TagsProvider<StructureSet> {

    public TutStructureSetTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, BuiltinRegistries.STRUCTURE_SETS, TutorialV3.MODID, helper, TagManager.getTagDir(BuiltinRegistries.STRUCTURE_SETS.key()).substring(5));
    }

    @Override
    protected void addTags() {
        tag(Registration.MYSTERIOUS_DIMENSION_STRUCTURE_SET)
                .add(ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(TutorialV3.MODID, "portal")))
                .add(ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(TutorialV3.MODID, "thiefden")))
        ;
    }

    @Override
    public String getName() {
        return "Tutorial Tags";
    }
}

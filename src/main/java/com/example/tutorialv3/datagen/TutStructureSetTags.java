package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TutStructureSetTags extends TagsProvider<StructureSet> {

    public TutStructureSetTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(packOutput, Registries.STRUCTURE_SET, lookupProvider, TutorialV3.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Registration.MYSTERIOUS_DIMENSION_STRUCTURE_SET)
                .add(ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(TutorialV3.MODID, "portal")))
                .add(ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(TutorialV3.MODID, "thiefden")))
        ;
    }

    @Override
    public String getName() {
        return "Tutorial Structure Set Tags";
    }
}

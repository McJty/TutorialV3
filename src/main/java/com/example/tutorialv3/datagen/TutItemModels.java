package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import net.minecraft.data.DataGenerator;
//import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TutItemModels extends ItemModelProvider {

    public TutItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TutorialV3.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.MYSTERIOUS_ORE_OVERWORLD_ITEM.get().getRegistryName().getPath(), modLoc("block/mysterious_ore_overworld"));
        withExistingParent(Registration.MYSTERIOUS_ORE_NETHER_ITEM.get().getRegistryName().getPath(), modLoc("block/mysterious_ore_nether"));
        withExistingParent(Registration.MYSTERIOUS_ORE_END_ITEM.get().getRegistryName().getPath(), modLoc("block/mysterious_ore_end"));
        withExistingParent(Registration.MYSTERIOUS_ORE_DEEPSLATE_ITEM.get().getRegistryName().getPath(), modLoc("block/mysterious_ore_deepslate"));

        withExistingParent(Registration.GENERATOR_ITEM.get().getRegistryName().getPath(), modLoc("block/generator"));
        withExistingParent(Registration.POWERGEN_ITEM.get().getRegistryName().getPath(), modLoc("block/powergen/main"));
        withExistingParent(Registration.PORTAL_ITEM.get().getRegistryName().getPath(), modLoc("block/portal"));

        withExistingParent(Registration.THIEF_EGG.get().getRegistryName().getPath(), mcLoc("item/template_spawn_egg"));

        singleTexture(Registration.RAW_MYSTERIOUS_CHUNK.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/raw_mysterious_chunk"));
        singleTexture(Registration.MYSTERIOUS_INGOT.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/mysterious_ingot"));
    }
}

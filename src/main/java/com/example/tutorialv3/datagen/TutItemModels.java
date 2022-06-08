package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class TutItemModels extends ItemModelProvider {

    public TutItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TutorialV3.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.MYSTERIOUS_ORE_OVERWORLD_ITEM.get()).getPath(), modLoc("block/mysterious_ore_overworld"));
        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.MYSTERIOUS_ORE_NETHER_ITEM.get()).getPath(), modLoc("block/mysterious_ore_nether"));
        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.MYSTERIOUS_ORE_END_ITEM.get()).getPath(), modLoc("block/mysterious_ore_end"));
        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.MYSTERIOUS_ORE_DEEPSLATE_ITEM.get()).getPath(), modLoc("block/mysterious_ore_deepslate"));

        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.GENERATOR_ITEM.get()).getPath(), modLoc("block/generator"));
        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.POWERGEN_ITEM.get()).getPath(), modLoc("block/powergen/main"));
        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.PORTAL_ITEM.get()).getPath(), modLoc("block/portal"));

        withExistingParent(ForgeRegistries.ITEMS.getKey(Registration.THIEF_EGG.get()).getPath(), mcLoc("item/template_spawn_egg"));

        singleTexture(ForgeRegistries.ITEMS.getKey(Registration.RAW_MYSTERIOUS_CHUNK.get()).getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/raw_mysterious_chunk"));
        singleTexture(ForgeRegistries.ITEMS.getKey(Registration.MYSTERIOUS_INGOT.get()).getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/mysterious_ingot"));
    }
}

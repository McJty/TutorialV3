package com.example.tutorialv3.datagen;

import com.example.tutorialv3.TutorialV3;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TutorialV3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeServer(), new TutRecipes(generator));
        generator.addProvider(event.includeServer(), new TutLootTables(generator));
        TutBlockTags blockTags = new TutBlockTags(generator, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new TutItemTags(generator, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TutBiomeTags(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TutStructureSetTags(generator, event.getExistingFileHelper()));

        generator.addProvider(event.includeClient(), new TutBlockStates(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new TutItemModels(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new TutLanguageProvider(generator, "en_us"));
    }
}

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
        if (event.includeServer()) {
            generator.addProvider(new TutRecipes(generator));
            generator.addProvider(new TutLootTables(generator));
            TutBlockTags blockTags = new TutBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new TutItemTags(generator, blockTags, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.addProvider(new TutBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new TutItemModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new TutLanguageProvider(generator, "en_us"));
        }
    }
}

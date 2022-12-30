package com.example.tutorialv3.datagen;

import com.example.tutorialv3.setup.Registration;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class TutLootTables implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> builder) {
        builder.accept(Registration.GENERATOR.getId(), LootTableTools.createStandardTable("generator", Registration.GENERATOR.get(), Registration.GENERATOR_BE.get()));
        builder.accept(Registration.POWERGEN.getId(), LootTableTools.createStandardTable("powergen", Registration.POWERGEN.get(), Registration.POWERGEN_BE.get()));
        builder.accept(Registration.MYSTERIOUS_ORE_OVERWORLD.getId(), LootTableTools.createSilkTouchTable("mysterious_ore_overworld", Registration.MYSTERIOUS_ORE_OVERWORLD.get(), Registration.RAW_MYSTERIOUS_CHUNK.get(), 1, 3));
        builder.accept(Registration.MYSTERIOUS_ORE_NETHER.getId(), LootTableTools.createSilkTouchTable("mysterious_ore_nether", Registration.MYSTERIOUS_ORE_NETHER.get(), Registration.RAW_MYSTERIOUS_CHUNK.get(), 1, 3));
        builder.accept(Registration.MYSTERIOUS_ORE_END.getId(), LootTableTools.createSilkTouchTable("mysterious_ore_end", Registration.MYSTERIOUS_ORE_END.get(), Registration.RAW_MYSTERIOUS_CHUNK.get(), 1, 3));
        builder.accept(Registration.MYSTERIOUS_ORE_DEEPSLATE.getId(), LootTableTools.createSilkTouchTable("mysterious_ore_deepslate", Registration.MYSTERIOUS_ORE_DEEPSLATE.get(), Registration.RAW_MYSTERIOUS_CHUNK.get(), 1, 3));
    }
}

package com.example.tutorialv3.datagen;

import com.example.tutorialv3.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class TutRecipes extends RecipeProvider {

    public TutRecipes(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.GENERATOR.get())
                .pattern("mxm")
                .pattern("x#x")
                .pattern("#x#")
                .define('x', Tags.Items.GEMS_DIAMOND)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('m', Registration.MYSTERIOUS_INGOT.get())
                .group("tutorialv3")
                .unlockedBy("mysterious", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.MYSTERIOUS_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.POWERGEN.get())
                .pattern("mmm")
                .pattern("x#x")
                .pattern("#x#")
                .define('x', Tags.Items.DUSTS_REDSTONE)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('m', Registration.MYSTERIOUS_INGOT.get())
                .group("tutorialv3")
                .unlockedBy("mysterious", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.MYSTERIOUS_INGOT.get()))
                .save(consumer);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.MYSTERIOUS_ORE_ITEM), RecipeCategory.MISC,
                        Registration.MYSTERIOUS_INGOT.get(), 1.0f, 100)
                .unlockedBy("has_ore", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.MYSTERIOUS_ORE_ITEM).build()))
                .save(consumer, "mysterious_ingot1");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.RAW_MYSTERIOUS_CHUNK.get()), RecipeCategory.MISC,
                        Registration.MYSTERIOUS_INGOT.get(), 0.0f, 100)
                .unlockedBy("has_chunk", has(Registration.RAW_MYSTERIOUS_CHUNK.get()))
                .save(consumer, "mysterious_ingot2");
    }
}

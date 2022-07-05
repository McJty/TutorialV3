package com.example.tutorialv3.datagen;

import java.io.IOException;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public record TutBiomeModifiers(DataGenerator generator) implements DataProvider {

    @Override
    public void run(CachedOutput cache) throws IOException {
//        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
//        Registry<PlacedFeature> placedFeatures = ops.registry(Registry.PLACED_FEATURE_REGISTRY).get();
//        final Path outputFolder = generator.getOutputFolder();
//        final String directory = PackType.SERVER_DATA.getDirectory();
//        final String featurePathString = String.join("/", directory, MODID, Registry.PLACED_FEATURE_REGISTRY.location().getPath(), "ore_overworld.json");
//        final Path featurePath = outputFolder.resolve(featurePathString);
//        final String biomeModifierPathString = String.join("/", directory, MODID, Registration.ORE_BIOME_MODIFIER.getId().getNamespace(), Registration.ORE_BIOME_MODIFIER.getId().getPath(), "ore_modifiers.json");
//        final Path biomeModifierPath = outputFolder.resolve(biomeModifierPathString);
//
//        PlacedFeature.DIRECT_CODEC.encodeStart(ops, Registration.ORE_OVERWORLD.get())
//                .resultOrPartial(msg -> TutorialV3.LOGGER.error("Failed to encode {}: {}", featurePathString, msg))
//                .ifPresent(json -> {
//                    try {
//                        DataProvider.saveStable(cache, json, featurePath);
//                    } catch (IOException e) {
//                        TutorialV3.LOGGER.error("Failed to save " + featurePathString, e);
//                    }
//                });
//        BiomeModifier biomeModifier = new OreBiomeModifier(
//                new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD),
//                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
//                HolderSet.direct(placedFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Registration.ORE_OVERWORLD.getId()))));
//        BiomeModifier.DIRECT_CODEC.encodeStart(ops, biomeModifier)
//                .resultOrPartial(msg -> TutorialV3.LOGGER.error("Failed to encode {}: {}", biomeModifierPathString, msg)) // Log error on encode failure.
//                .ifPresent(json -> {
//                    try {
//                        DataProvider.saveStable(cache, json, biomeModifierPath);
//                    } catch (IOException e) {
//                        TutorialV3.LOGGER.error("Failed to save " + biomeModifierPathString, e);
//                    }
//                });
//        ;
    }

    @Override
    public String getName() {
        return "Tutorial Biome Modifiers";
    }
}

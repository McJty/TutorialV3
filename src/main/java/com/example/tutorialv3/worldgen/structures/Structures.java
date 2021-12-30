package com.example.tutorialv3.worldgen.structures;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.setup.Registration;
import com.example.tutorialv3.worldgen.dimensions.Dimensions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Structures {
    /**
     * Static instances of our structures so we can reference it and add it to biomes easily.
     * We cannot get our own pool here at mod init so we use PlainVillagePools.START.
     * We will modify this pool at runtime later in createPiecesGenerator
     */
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_THIEFDEN = Registration.THIEFDEN.get()
            .configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_PORTAL_OVERWORLD = Registration.PORTAL_OVERWORLD.get()
            .configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_PORTAL_MYSTERIOUS = Registration.PORTAL_MYSTERIOUS.get()
            .configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));

    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     *
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(TutorialV3.MODID, "thiefden"), CONFIGURED_THIEFDEN);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(TutorialV3.MODID, "portal_overworld"), CONFIGURED_PORTAL_OVERWORLD);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(TutorialV3.MODID, "portal_mysterious"), CONFIGURED_PORTAL_MYSTERIOUS);
    }

    /**
     * This is where we set the rarity of your structures and determine if land conforms to it.
     * See the comments in below for more details. This is also called from FMLCommonSetupEvent.
     */
    public static void setupStructures() {
        setupMapSpacingAndLand(
                Registration.THIEFDEN.get(),
                new StructureFeatureConfiguration(10, // average distance apart in chunks between spawn attempts
                        5,            // minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE
                        1234567890),  // this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */
                true);

        setupMapSpacingAndLand(
                Registration.PORTAL_OVERWORLD.get(),
                new StructureFeatureConfiguration(10,5,1294567890),
                false);

        setupMapSpacingAndLand(
                Registration.PORTAL_MYSTERIOUS.get(),
                new StructureFeatureConfiguration(10,5,1294567890), // By using the same seed our portals in the overworld and other dimension will be at the same spot
                true);
    }

    /**
     * Adds the provided structure to the registry, and adds the separation settings.
     * The rarity of the structure is determined based on the values passed into
     * this method in the StructureFeatureConfiguration argument.
     * This method is called by setupStructures above.
     */
    private static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            StructureFeatureConfiguration structureFeatureConfiguration,
            boolean transformSurroundingLand)
    {
        // Add our own structure into the structure feature map. Otherwise you get errors
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        // Adapt the surrounding land to the bottom of our structure
        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        // This is the map that holds the default spacing of all structures. This is normally
        // private and final. That's why we need an access transformer.
        // Always add your structure to here so that other mods can utilize it if needed
        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build();


        // Add our structure to all the noise generator settings.
        // structureConfig requires AccessTransformer
        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

            // Be careful with mods that make the structure map immutable (like datapacks do)
            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureFeatureConfiguration);
                settings.getValue().structureSettings().structureConfig = tempMap;
            } else {
                structureMap.put(structure, structureFeatureConfiguration);
            }
        });
    }

    /**
     * Tells the chunkgenerator which biomes our structure can spawn in.
     * Will go into the world's chunkgenerator where we manually add our structure spacing.
     * If the spacing is not added, the structure doesn't spawn.
     *
     * Use this for dimension blacklists for your structure.
     * (Don't forget to attempt to remove your structure too from the map if you are blacklisting that dimension!)
     * (It might have your structure in it already.)
     *
     * Basically use this to make absolutely sure the chunkgenerator can or cannot spawn your structure.
     */
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
            // Skip superflat to prevent issues with it. Plus, users don't want structures clogging up their superflat worlds.
            if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
                return;
            }

            ConfiguredStructureFeature<?, ?> portalFeature = null;
            if (serverLevel.dimension().equals(Level.OVERWORLD)) {
                portalFeature = CONFIGURED_PORTAL_OVERWORLD;
            } else if (serverLevel.dimension().equals(Dimensions.MYSTERIOUS)) {
                portalFeature = CONFIGURED_PORTAL_MYSTERIOUS;
            }

            StructureSettings worldStructureConfig = chunkGenerator.getSettings();

            /*
             * NOTE: BiomeLoadingEvent from Forge API does not work with structures anymore.
             * Instead, we will use the below to add our structure to overworld biomes.
             * Remember, this is temporary until Forge API finds a better solution for adding structures to biomes.
             */

            // Create a mutable map we will use for easier adding to biomes
            var structureToMultimap = new HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>>();

            // Add the resourcekey of all biomes that this Configured Structure can spawn in.
            for (var biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {
                // Skip all ocean, end, nether, and none category biomes.
                // You can do checks for other traits that the biome has.
                BiomeCategory category = biomeEntry.getValue().getBiomeCategory();
                if (category != BiomeCategory.OCEAN && category != BiomeCategory.THEEND && category != BiomeCategory.NETHER && category != BiomeCategory.NONE) {
                    associateBiomeToConfiguredStructure(structureToMultimap, CONFIGURED_THIEFDEN, biomeEntry.getKey());
                }
                if (portalFeature != null) {
                    if (category != BiomeCategory.THEEND && category != BiomeCategory.NETHER && category != BiomeCategory.NONE) {
                        associateBiomeToConfiguredStructure(structureToMultimap, portalFeature, biomeEntry.getKey());
                    }
                }
            }

            // Grab the map that holds what ConfigureStructures a structure has and what biomes it can spawn in.
            // Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet()
                    .stream()
                    .filter(entry -> !structureToMultimap.containsKey(entry.getKey()))
                    .forEach(tempStructureToMultiMap::put);

            // Add our structures to the structure map/multimap and set the world to use this combined map/multimap.
            structureToMultimap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

            // Requires AccessTransformer (see resources/META-INF/accesstransformer.cfg)
            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();
        }
    }

    /**
     * Helper method that handles setting up the map to multimap relationship to help prevent issues.
     */
    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> structureToMultimap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        structureToMultimap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        var configuredStructureToBiomeMultiMap = structureToMultimap.get(configuredStructureFeature.feature);
        if (configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            TutorialV3.LOGGER.error("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """,
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
                    biomeRegistryKey
            );
        } else {
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
        }
    }

    /**
     * Create a copy of a piece generator context with another config. This is used by the structures
     */
    @NotNull
    static PieceGeneratorSupplier.Context<JigsawConfiguration> createContextWithConfig(PieceGeneratorSupplier.Context<JigsawConfiguration> context, JigsawConfiguration newConfig) {
        return new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );
    }

    private static final Lazy<List<MobSpawnSettings.SpawnerData>> STRUCTURE_MONSTERS = Lazy.of(() -> ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityType.ILLUSIONER, 200, 4, 9),
            new MobSpawnSettings.SpawnerData(EntityType.VINDICATOR, 200, 4, 9)
    ));

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if (event.getStructure() == Registration.PORTAL_OVERWORLD.get() || event.getStructure() == Registration.PORTAL_MYSTERIOUS.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, STRUCTURE_MONSTERS.get());
        }
    }
}

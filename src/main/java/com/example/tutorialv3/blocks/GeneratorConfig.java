package com.example.tutorialv3.blocks;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneratorConfig {

    public static ForgeConfigSpec.IntValue COLLECTING_DELAY;
    public static ForgeConfigSpec.IntValue INGOTS_PER_ORE;
    public static ForgeConfigSpec.IntValue ENERGY_CAPACITY;
    public static ForgeConfigSpec.IntValue ENERGY_RECEIVE;
    public static ForgeConfigSpec.IntValue ENERGY_GENERATE;

    public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Settings for the generator").push("generator");

        COLLECTING_DELAY = SERVER_BUILDER
                .comment("Delay (in ticks) before collecting items")
                .defineInRange("collectingDelay", 10, 1, Integer.MAX_VALUE);
        INGOTS_PER_ORE = SERVER_BUILDER
                .comment("How many ingots you get from one ore")
                .defineInRange("ingotsPerOre", 10, 1, Integer.MAX_VALUE);
        ENERGY_CAPACITY = SERVER_BUILDER
                .comment("How much energy fits into the generator")
                .defineInRange("capacity", 100000, 1, Integer.MAX_VALUE);
        ENERGY_RECEIVE = SERVER_BUILDER
                .comment("How much energy the generator can receive per side")
                .defineInRange("receive", 1000, 1, Integer.MAX_VALUE);
        ENERGY_GENERATE = SERVER_BUILDER
                .comment("How much energy is needed to process one ore block")
                .defineInRange("generate", 500, 1, Integer.MAX_VALUE);

        SERVER_BUILDER.pop();
    }
}

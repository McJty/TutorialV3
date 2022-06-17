package com.example.tutorialv3.worldgen.ores;

import net.minecraftforge.common.ForgeConfigSpec;

public class OresConfig {

    public static ForgeConfigSpec.IntValue MYSTERIOUS_VEINSIZE;
    public static ForgeConfigSpec.IntValue MYSTERIOUS_AMOUNT;
    public static ForgeConfigSpec.IntValue OVERWORLD_VEINSIZE;
    public static ForgeConfigSpec.IntValue OVERWORLD_AMOUNT;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Settings for ore generation").push("ores");

        MYSTERIOUS_VEINSIZE = COMMON_BUILDER
                .comment("Veinsize of our ore in the mysterious dimension")
                .defineInRange("mysteriousVeinsize", 25, 1, Integer.MAX_VALUE);
        MYSTERIOUS_AMOUNT = COMMON_BUILDER
                .comment("Amount of veines of our ore in the mysterious dimension")
                .defineInRange("mysteriousAmount", 10, 1, Integer.MAX_VALUE);
        OVERWORLD_VEINSIZE = COMMON_BUILDER
                .comment("Veinsize of our ore in the overworld dimension")
                .defineInRange("overworldVeinsize", 5, 1, Integer.MAX_VALUE);
        OVERWORLD_AMOUNT = COMMON_BUILDER
                .comment("Amount of veines of our ore in the overworld dimension")
                .defineInRange("overworldAmount", 3, 1, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();
    }

}

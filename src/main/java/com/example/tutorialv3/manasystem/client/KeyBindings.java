package com.example.tutorialv3.manasystem.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyBindings {
    public static final String KEY_CATEGORIES_TUTORIAL = "key.categories.tutorial";
    public static final String KEY_GATHER_MANA = "key.gatherMana";

    public static KeyMapping gatherManaKeyMapping;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(gatherManaKeyMapping = new KeyMapping(KEY_GATHER_MANA, InputConstants.KEY_PERIOD, KEY_CATEGORIES_TUTORIAL));
    }
}

package com.example.tutorialv3.manasystem.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;


public class KeyBindings {

    public static final String KEY_CATEGORIES_TUTORIAL = "key.categories.tutorial";
    public static final String KEY_GATHER_MANA = "key.gatherMana";

    public static KeyMapping gatherManaKeyMapping;

    public static void init(RegisterKeyMappingsEvent event) {
        gatherManaKeyMapping = new KeyMapping(KEY_GATHER_MANA, KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.period"), KEY_CATEGORIES_TUTORIAL);
        event.register(gatherManaKeyMapping);
    }
}

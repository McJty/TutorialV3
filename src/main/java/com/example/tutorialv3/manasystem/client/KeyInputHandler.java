package com.example.tutorialv3.manasystem.client;

import com.example.tutorialv3.manasystem.network.PacketGatherMana;
import com.example.tutorialv3.setup.Messages;
import net.minecraftforge.client.event.InputEvent;

public class KeyInputHandler {

    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.gatherManaKeyMapping.consumeClick()) {
            Messages.sendToServer(new PacketGatherMana());
        }
    }
}

package com.narrowvertex.cos.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.narrowvertex.cos.ClientOnlySorterClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class KeyBindings {

    public static KeyMapping sortKey = new KeyMapping(
            "key.cos.sort",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_Y,
            "key.cos.category");

    public void register() {
        ClientOnlySorterClient client = ClientOnlySorterClient.getInstance();

        ClientOnlySorterClient.LOGGER.info("Key Registered!");
    }

    private void register(KeyMapping keyMapping, KeyBehavior behavior) {
        keyMapping = KeyBindingHelper.registerKeyBinding(keyMapping);

        KeyMapping finalKeyMapping = keyMapping;
        ClientTickEvents.END_CLIENT_TICK.register(m -> {
            while(finalKeyMapping.consumeClick()) {
                behavior.action();
            }
        });
    }

    interface KeyBehavior {
        void action();
    }
}

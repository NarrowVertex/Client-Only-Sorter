package com.narrowvertex.cos;

import com.narrowvertex.cos.input.KeyBindings;
import net.fabricmc.api.ModInitializer;

public class ClientOnlySorter implements ModInitializer {

    public static final String MODID = "cos";

    private ClientOnlySorterClient client;
    private KeyBindings keyBindings;

    @Override
    public void onInitialize() {
        client = new ClientOnlySorterClient();
        keyBindings = new KeyBindings();

        client.init();
        keyBindings.register();
    }
}

package com.narrowvertex.cos.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.narrowvertex.cos.ClientOnlySorterClient;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class KeyBindings {

    public static KeyMapping sortKey = new KeyMapping(
            "key.cos.sort",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_Y,
            "key.cos.category");

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(KeyBindings::registerKeyBindings);
    }

    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(sortKey);
        ClientOnlySorterClient.LOGGER.info("Key Registered!");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        ClientOnlySorterClient client = ClientOnlySorterClient.getInstance();

        if(event.phase == TickEvent.Phase.END) {
            while(sortKey.consumeClick()) {
                client.sort();
            }
        }
    }
}

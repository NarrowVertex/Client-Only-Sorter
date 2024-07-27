package com.narrowvertex.cos.input;

import com.narrowvertex.cos.ClientOnlySorterClient;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class KeyBindings {

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(KeyBindings::registerKeyBindings);
    }

    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        ClientOnlySorterClient.LOGGER.info("Key Registered!");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

    }
}

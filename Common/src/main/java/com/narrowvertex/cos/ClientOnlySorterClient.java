package com.narrowvertex.cos;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.narrowvertex.cos.mixin.ContainerScreenInterfaceMixin;
import com.narrowvertex.cos.mixin.KeyMappingMixin;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

public class ClientOnlySorterClient {

    private static ClientOnlySorterClient instance;

    public static Logger LOGGER = LogUtils.getLogger();

    public static NonNullList<Slot> slots;

    public ClientOnlySorterClient() {
        instance = this;
    }

    public void init() {

    }

    public void sort(int x, int y) {
        Minecraft mc = Minecraft.getInstance();

        if(mc.player == null)
            return;

        Screen currentScreen = mc.screen;
        if(currentScreen == null)
            return;

        if(!(currentScreen instanceof ContainerScreen containerScreen))
            return;
//
//        Slot slot = slots.get(0);
//        ItemStack itemStack = slot.getItem();
//        int id = Item.getId(itemStack.getItem());
//
//        containerScreen.mouseClicked(x + slot.x + 4, y + slot.y + 4, 0);
//        containerScreen.mouseReleased(x + slot.x + 4, y + slot.y + 4, 0);

        // sortSlots(containerScreen, x, y, slots);

        SortThread sortThread = new SortThread(containerScreen, x, y, slots);
        sortThread.start();
    }

    static class SortThread extends Thread {
        ContainerScreen containerScreen;
        int x;
        int y;
        NonNullList<Slot> slots;

        public SortThread(ContainerScreen containerScreen, int x, int y, NonNullList<Slot> slots) {
            this.containerScreen = containerScreen;
            this.x = x;
            this.y = y;
            this.slots = slots;
        }

        @Override
        public void run() {
            sortSlots(containerScreen, x, y, slots);
        }
    }

    public static void sortSlots(ContainerScreen containerScreen, int x, int y, NonNullList<Slot> slots) {
        int n = slots.size();

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(containerScreen, x, y, slots, n, i);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            swap(containerScreen, x, y, slots.get(0), slots.get(i));
            // Swap in the list
//            Slot temp = slots.get(0);
//            slots.set(0, slots.get(i));
//            slots.set(i, temp);

            // Call max heapify on the reduced heap
            heapify(containerScreen, x, y, slots, i, 0);
        }
    }

    private static void heapify(ContainerScreen containerScreen, int x, int y, NonNullList<Slot> slots, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // left = 2*i + 1
        int right = 2 * i + 2; // right = 2*i + 2

        // Get the id of items in slots
        ItemStack itemStackLargest = slots.get(largest).getItem();
        int idLargest = getID(itemStackLargest);

        if (left < n) {
            ItemStack itemStackLeft = slots.get(left).getItem();
            int idLeft = getID(itemStackLeft);
            if (idLeft > idLargest) {
                largest = left;
                idLargest = idLeft;
            }
        }

        if (right < n) {
            ItemStack itemStackRight = slots.get(right).getItem();
            int idRight = getID(itemStackRight);
            if (idRight > idLargest) {
                largest = right;
            }
        }

        // If largest is not root
        if (largest != i) {

            // Perform the swap in the ContainerScreen
            swap(containerScreen, x, y, slots.get(i), slots.get(largest));

            // Recursively heapify the affected sub-tree
            heapify(containerScreen, x, y, slots, n, largest);
        }
    }

    static int getID(ItemStack itemStack) {
        int id = Item.getId(itemStack.getItem());
        if(id == 0)
            id = 99999;
        return id;
    }

    static void swap(ContainerScreen containerScreen, int x, int y, Slot slotA, Slot slotB) {
        Minecraft mc = Minecraft.getInstance();

        if(mc.player == null)
            return;

        Screen currentScreen = mc.screen;
        if(currentScreen == null)
            return;

        if(!(currentScreen instanceof ContainerScreen))
            return;

//        containerScreen.mouseClicked(x + slotA.x + 4, y + slotA.y + 4, 0);
//        containerScreen.mouseReleased(x + slotA.x + 4, y + slotA.y + 4, 0);
//        containerScreen.mouseClicked(x + slotB.x + 4, y + slotB.y + 4, 0);
//        containerScreen.mouseReleased(x + slotB.x + 4, y + slotB.y + 4, 0);
//        containerScreen.mouseClicked(x + slotA.x + 4, y + slotA.y + 4, 0);
//        containerScreen.mouseReleased(x + slotA.x + 4, y + slotA.y + 4, 0);
        // containerScreen.keyPressed()
        int swapKey = getSwapKey();


        Slot tempSlot = ((ContainerScreenInterfaceMixin) containerScreen).getSlot();

        ((ContainerScreenInterfaceMixin) containerScreen).setSlot(slotA);
        containerScreen.keyPressed(swapKey, getScancode(swapKey), 0);

        ((ContainerScreenInterfaceMixin) containerScreen).setSlot(slotB);
        containerScreen.keyPressed(swapKey, getScancode(swapKey), 0);

        ((ContainerScreenInterfaceMixin) containerScreen).setSlot(slotA);
        containerScreen.keyPressed(swapKey, getScancode(swapKey), 0);

        ((ContainerScreenInterfaceMixin) containerScreen).setSlot(tempSlot);

        try {
            Thread.sleep(100);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int getSwapKey() {
        return ((KeyMappingMixin)Minecraft.getInstance().options.keySwapOffhand).getKey().getValue();
    }

    static int getScancode(int keyCode) {
        return InputConstants.Type.SCANCODE.getOrCreate(keyCode).getValue();
    }

    public static ClientOnlySorterClient getInstance() {
        return instance;
    }
}

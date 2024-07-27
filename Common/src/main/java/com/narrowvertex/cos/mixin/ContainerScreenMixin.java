package com.narrowvertex.cos.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.narrowvertex.cos.ClientOnlySorterClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.*;

@Mixin(ContainerScreen.class)
public class ContainerScreenMixin extends AbstractContainerScreen<ChestMenu> implements MenuAccess<ChestMenu> {
    @Unique
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");
    @Mutable
    @Shadow
    @Final
    private final int containerRows;

    public ContainerScreenMixin(ChestMenu $$0, Inventory $$1, Component $$2) {
        super($$0, $$1, $$2);
        int $$3 = 222;
        int $$4 = 114;
        this.containerRows = $$0.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        ClientOnlySorterClient.slots = excludeLastNElements(this.menu.slots, 36);
        /*
        for (int m = 0; m < (this.menu).slots.size(); ++m) {
            Slot slot = (this.menu).slots.get(m);
            System.out.println(slot.getItem().getDisplayName().getString());
        }
         */
    }

    @Unique
    public NonNullList<Slot> excludeLastNElements(NonNullList<Slot> list, int n) {
        if (n >= list.size()) {
            return NonNullList.create(); // n이 리스트 크기보다 크거나 같으면 빈 리스트 반환
        }

        NonNullList<Slot> newSlots = NonNullList.create();
        for(int i = 0; i < list.size() - n; i++) {
            newSlots.add(i, list.get(i));
        }
        return newSlots;
    }

    @Override
    public boolean keyPressed(int a, int b, int c) {
        if (a == InputConstants.KEY_Y) {
            ClientOnlySorterClient.getInstance().sort(leftPos, topPos);
        }
        return super.keyPressed(a, b, c);
    }

    /*
    @Override
    public boolean mouseClicked(double a, double b, int c) {
        System.out.printf("mouseClicked: {%f, %f, %d} \n", a, b, c);
        return super.mouseClicked(a, b, c);
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        System.out.printf("mouseReleased: {%f, %f, %d} \n", $$0, $$1, $$2);
        return super.mouseReleased($$0, $$1, $$2);
    }

    @Override
    public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
        System.out.printf("mouseDragged: {%f, %f, %d, %f, %f} \n", $$0, $$1, $$2, $$3, $$4);
        return super.mouseDragged($$0, $$1, $$2, $$3, $$4);
    }
     */

    @Override
    protected void renderBg(GuiGraphics $$0, float $$1, int $$2, int $$3) {
        int $$4 = (this.width - this.imageWidth) / 2;
        int $$5 = (this.height - this.imageHeight) / 2;
        $$0.blit(CONTAINER_BACKGROUND, $$4, $$5, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        $$0.blit(CONTAINER_BACKGROUND, $$4, $$5 + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}

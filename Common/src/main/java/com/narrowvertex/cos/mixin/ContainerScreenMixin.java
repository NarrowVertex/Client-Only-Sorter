package com.narrowvertex.cos.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.narrowvertex.cos.ClientOnlySorterClient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class ContainerScreenMixin {

    @Shadow
    @Final
    protected AbstractContainerMenu menu;

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    @Inject(method = "init()V", at=@At("TAIL"))
    void init(CallbackInfo ci) {
        ClientOnlySorterClient.slots = excludeLastNElements(this.menu.slots, 36);
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

    @Inject(method = "keyPressed(III)Z", at=@At("HEAD"))
    public void keyPressed(int $$0, int $$1, int $$2, CallbackInfoReturnable<Boolean> cir) {
        if ($$0 == InputConstants.KEY_Y) {
            ClientOnlySorterClient.getInstance().sort(leftPos, topPos);
        }
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
}

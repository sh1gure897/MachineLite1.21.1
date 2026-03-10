package com.lite.machinelite.mixin.client;

import com.lite.machinelite.event.impl.RightClickMouseEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraft {
    @Inject(method = "doItemUse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I"), cancellable = true)
    private void rightClickMouse(CallbackInfo ci) {
        RightClickMouseEvent rightClickMouseEvent = new RightClickMouseEvent();
        rightClickMouseEvent.call();

        if (rightClickMouseEvent.isCancelled()) {
            ci.cancel();
        }
    }
}


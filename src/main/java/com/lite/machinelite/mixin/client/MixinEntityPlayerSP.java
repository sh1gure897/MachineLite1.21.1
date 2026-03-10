package com.lite.machinelite.mixin.client;

import com.lite.machinelite.event.EventType;
import com.lite.machinelite.event.impl.UpdateEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinEntityPlayerSP {
    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    private void PreUpdateWalkingPlayer(CallbackInfo ci) {
        UpdateEvent updateEvent = new UpdateEvent();
        updateEvent.fire(EventType.PRE).call();
    }

    @Inject(method = "sendMovementPackets", at = @At("RETURN"))
    private void PostUpdateWalkingPlayer(CallbackInfo ci) {
        UpdateEvent updateEvent = new UpdateEvent();
        updateEvent.fire(EventType.POST).call();
    }
}


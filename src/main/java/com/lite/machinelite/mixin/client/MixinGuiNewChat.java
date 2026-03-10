package com.lite.machinelite.mixin.client;

import com.google.common.collect.Lists;
import com.lite.machinelite.event.impl.ChatInputEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public class MixinGuiNewChat {
    @Final
    @Shadow
    private final List<ChatHudLine.Visible> visibleMessages = Lists.newArrayList();

    private static boolean preventRecursion = false;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    public void addMessage(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        if (preventRecursion)
            return;

        ChatInputEvent chatInputEvent = new ChatInputEvent(message, visibleMessages);
        chatInputEvent.call();

        if (chatInputEvent.isCancelled()) {
            ci.cancel();
            if (chatInputEvent.isModified()) {
                preventRecursion = true;
                net.minecraft.client.MinecraftClient.getInstance().inGameHud.getChatHud()
                        .addMessage(chatInputEvent.getTextComponent());
                preventRecursion = false;
            }
        }
    }
}

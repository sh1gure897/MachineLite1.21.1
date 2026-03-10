package com.lite.machinelite.event.impl;

import com.lite.machinelite.event.Event;
import net.minecraft.client.gui.DrawContext;

public class RenderOverlayEvent extends Event {
    private DrawContext context;
    private float partialTicks;

    public RenderOverlayEvent fire(DrawContext context, float partialTicks) {
        this.context = context;
        this.partialTicks = partialTicks;
        return this;
    }

    public DrawContext getContext() {
        return context;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}


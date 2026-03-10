package com.lite.machinelite.mixin.client;

import net.minecraft.item.map.MapState;
import net.minecraft.item.map.MapDecoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MapState.class)
public interface IMixinMapData {
    @Accessor("decorations")
    Map<String, MapDecoration> getMapDecorations();
}


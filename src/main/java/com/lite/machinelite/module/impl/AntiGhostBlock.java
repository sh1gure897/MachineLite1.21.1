package com.lite.machinelite.module.impl;

import com.lite.machinelite.event.Event;
import com.lite.machinelite.event.impl.EventBreakBlock;
import com.lite.machinelite.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.Direction;

public class AntiGhostBlock extends Module {
    public AntiGhostBlock(String name, int keyCode) {
        super(name, keyCode);
    }

    @Override
    public void onEvent(Event event) {
        if (!isEnabled())
            return;

        if (event instanceof EventBreakBlock) {
            EventBreakBlock breakEvent = (EventBreakBlock) event;
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                    PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, breakEvent.getPos(), Direction.UP));
        }
    }
}

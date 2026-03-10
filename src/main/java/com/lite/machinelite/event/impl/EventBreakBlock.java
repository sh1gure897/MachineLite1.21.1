package com.lite.machinelite.event.impl;

import com.lite.machinelite.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class EventBreakBlock extends Event {
    private BlockState state;
    private BlockPos pos;

    public Event fire(BlockPos pos, BlockState state) {
        this.pos = pos;
        this.state = state;
        return this;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }
}
package com.lite.machinelite.module.impl;

import com.lite.machinelite.event.Event;
import com.lite.machinelite.event.impl.UpdateEvent;
import com.lite.machinelite.module.Module;
import com.lite.machinelite.utilities.Utils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class AutoHighway extends Module {
    private final ArrayList<BlockPos> positions = new ArrayList<>();
    private Item lastBlockItem = null;

    public AutoHighway(String name, int keyCode) {
        super(name, keyCode);
    }

    @Override
    public void onEnabled() {
        this.positions.clear();
        this.lastBlockItem = null;
    }

    @Override
    public void onEvent(Event event) {
        if (!isEnabled())
            return;

        if (event instanceof UpdateEvent) {
            if (event.isPost()) {

                ItemStack mainHand = mc.player.getMainHandStack();
                if (Utils.isBuilderBlock(mainHand)) {
                    lastBlockItem = mainHand.getItem();
                } else if (lastBlockItem != null) {
                    if (!refillBlocks()) {
                        return; // Wait until blocks are found or just fail silently
                    }
                    // Re-check after refill attempt
                    mainHand = mc.player.getMainHandStack();
                    if (!Utils.isBuilderBlock(mainHand)) {
                        return;
                    }
                } else {
                    return; // No blocks in hand and no last block
                }

                final Vec3d playerPos = mc.player.getPos();
                BlockPos originPos = BlockPos.ofFloored(playerPos.x, playerPos.y + 0.5f, playerPos.z);

                if (positions.isEmpty()) {
                    switch (Utils.getFacing()) {
                        case EAST:
                            positions.add(originPos.down());
                            positions.add(originPos.down().east());
                            positions.add(originPos.down().east().north());
                            positions.add(originPos.down().east().south());
                            positions.add(originPos.down().east().north().north());
                            positions.add(originPos.down().east().south().south());
                            positions.add(originPos.down().east().north().north().north());
                            positions.add(originPos.down().east().south().south().south());
                            positions.add(originPos.down().east().north().north().north().up());
                            positions.add(originPos.down().east().south().south().south().up());
                            break;
                        case NORTH:
                            positions.add(originPos.down());
                            positions.add(originPos.down().north());
                            positions.add(originPos.down().north().east());
                            positions.add(originPos.down().north().west());
                            positions.add(originPos.down().north().east().east());
                            positions.add(originPos.down().north().west().west());
                            positions.add(originPos.down().north().east().east().east());
                            positions.add(originPos.down().north().west().west().west());
                            positions.add(originPos.down().north().east().east().east().up());
                            positions.add(originPos.down().north().west().west().west().up());
                            break;
                        case SOUTH:
                            positions.add(originPos.down());
                            positions.add(originPos.down().south());
                            positions.add(originPos.down().south().east());
                            positions.add(originPos.down().south().west());
                            positions.add(originPos.down().south().east().east());
                            positions.add(originPos.down().south().west().west());
                            positions.add(originPos.down().south().east().east().east());
                            positions.add(originPos.down().south().west().west().west());
                            positions.add(originPos.down().south().east().east().east().up());
                            positions.add(originPos.down().south().west().west().west().up());
                            break;
                        case WEST:
                            positions.add(originPos.down());
                            positions.add(originPos.down().west());
                            positions.add(originPos.down().west().north());
                            positions.add(originPos.down().west().south());
                            positions.add(originPos.down().west().north().north());
                            positions.add(originPos.down().west().south().south());
                            positions.add(originPos.down().west().north().north().north());
                            positions.add(originPos.down().west().south().south().south());
                            positions.add(originPos.down().west().north().north().north().up());
                            positions.add(originPos.down().west().south().south().south().up());
                            break;
                    }
                }

                if (this.positions.size() <= 64) {
                    for (BlockPos pos : this.positions) {
                        if (mc.world.getBlockState(pos).isReplaceable()) {
                            Utils.placeBlock(4, pos);
                        }
                    }

                    this.positions.clear();
                }
            }
        }
    }

    private boolean refillBlocks() {
        if (mc.player == null || mc.player.getInventory() == null)
            return false;

        int hotbarSlot = mc.player.getInventory().selectedSlot;

        // Search hotbar first
        for (int i = 0; i < 9; i++) {
            if (i == hotbarSlot)
                continue;
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == lastBlockItem) {
                Utils.switchItem(i);
                return true;
            }
        }

        // Search main inventory
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == lastBlockItem) {
                if (mc.interactionManager != null) {
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i, hotbarSlot,
                            SlotActionType.SWAP, mc.player);
                    return true;
                }
            }
        }

        return false;
    }
}

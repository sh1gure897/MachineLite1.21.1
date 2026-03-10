package com.lite.machinelite.module.impl;

import com.lite.machinelite.event.Event;
import com.lite.machinelite.event.impl.UpdateEvent;
import com.lite.machinelite.module.Module;
import com.lite.machinelite.utilities.TimerUtil;
import com.lite.machinelite.utilities.Utils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FloorBuilder extends Module {
    private final TimerUtil timer;

    public FloorBuilder(String name, int keyCode) {
        super(name, keyCode);
        timer = new TimerUtil();
    }

    @Override
    public void onEvent(Event event) {
        if (!isEnabled())
            return;

        if (event instanceof UpdateEvent) {
            if (!checkHeldItem()) {
                return;
            }

            Vec3d vec3d = mc.player.getPos();
            BlockPos originPos = BlockPos.ofFloored(vec3d.x, vec3d.y - 1, vec3d.z);
            int posX = originPos.getX();
            int posZ = originPos.getZ();
            int range = 3;

            for (int x = posX - range; x <= posX + range; x++) {
                for (int z = posZ - range; z <= posZ + range; z++) {
                    if (timer.delay(80)) {
                        final BlockPos targetPos = new BlockPos(x, originPos.getY(), z);
                        this.tryToPlaceBlock(range, targetPos);
                    }
                }
            }
        }
    }

    private void tryToPlaceBlock(double reach, BlockPos pos) {
        if (pos == null || !mc.world.getBlockState(pos).isReplaceable()) {
            return;
        }

        if (Utils.placeBlock(reach, pos)) {
            timer.reset();
        }
    }

    private boolean checkHeldItem() {
        if (mc.player == null)
            return false;
        return Utils.isBuilderBlock(mc.player.getMainHandStack());
    }
}

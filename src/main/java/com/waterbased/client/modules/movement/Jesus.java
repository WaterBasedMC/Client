package com.waterbased.client.modules.movement;

import com.waterbased.client.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.block.Blocks.*;

public class Jesus extends Module {

    private BlockPos lastCustomPlacedBlockPos = null;
    private Block lastCustomPlacedBlock = null;

    public Jesus() {
        super("Jesus", "Allows you to walk on water/lava", InputUtil.GLFW_KEY_J);
    }

    public void onMove(Entity entity) {
        // Block below entity
        BlockPos blockPos = entity.getBlockPos().down();
        Block blockBelow = entity.world.getBlockState(blockPos).getBlock();
        // check if lastCustomPlacedBlockPos is the cur pos, then do nothing
        if (lastCustomPlacedBlockPos != null && lastCustomPlacedBlockPos.equals(blockPos)) return;

        Block replacementBlock = null;
        if (blockBelow.equals(WATER)) {
            replacementBlock = BLUE_STAINED_GLASS;
        } else if (blockBelow.equals(LAVA)) {
            replacementBlock = ORANGE_STAINED_GLASS;
        }

        if (replacementBlock == null || (lastCustomPlacedBlockPos != null && !lastCustomPlacedBlockPos.equals(blockPos))) { // if new block but got a block to replace
            if (lastCustomPlacedBlock != null) {
                entity.world.setBlockState(lastCustomPlacedBlockPos, lastCustomPlacedBlock.getDefaultState());
                lastCustomPlacedBlock = null;
                lastCustomPlacedBlockPos = null;
            }
            if (replacementBlock == null) return;
        }
        lastCustomPlacedBlockPos = blockPos;
        lastCustomPlacedBlock = blockBelow;
        entity.world.setBlockState(lastCustomPlacedBlockPos, replacementBlock.getDefaultState());
    }

    @Override
    public void onEnable() {
        // check if im already in water/lava
        Entity entity = MinecraftClient.getInstance().player;
        assert entity != null;
        onMove(entity);
    }

    @Override
    public void onDisable() {
        if (lastCustomPlacedBlock != null) {
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.world.setBlockState(lastCustomPlacedBlockPos, lastCustomPlacedBlock.getDefaultState());
            lastCustomPlacedBlock = null;
            lastCustomPlacedBlockPos = null;
        }
    }

}

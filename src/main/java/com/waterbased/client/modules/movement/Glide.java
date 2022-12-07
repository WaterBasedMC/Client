package com.waterbased.client.modules.movement;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;

public class Glide extends Module {

    private int tickCounter = 0;
    private double glideDropSpeed = 0.101;

    public Glide() {
        super("Glide", "Allows you to glide", InputUtil.GLFW_KEY_V);
    }

    @Override
    public void onEnable() {
    }


    @Override
    public void onTick() {
        tickCounter++;
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (shouldGlide()) {
            player.setVelocity(player.getVelocity().x, tickCounter % 2 == 0 ? glideDropSpeed : -glideDropSpeed, player.getVelocity().z);
            // TODO: Reset resetpoint with invalid packet?
        }
    }

    public double getGlideDropSpeed() {
        return Math.max(0, glideDropSpeed);
    }

    public void setGlideDropSpeed(double glideDropSpeed) {
        if (glideDropSpeed < 0) {
            throw new IllegalArgumentException("Glide drop speed cannot be negative");
        }
        this.glideDropSpeed = glideDropSpeed;
    }

    private boolean shouldGlide() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return player != null && !player.isOnGround() && !player.isTouchingWater() && !player.isInLava();
    }

    @Override
    public void onDisable() {
    }

}

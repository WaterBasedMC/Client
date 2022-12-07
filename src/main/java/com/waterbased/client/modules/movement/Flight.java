package com.waterbased.client.modules.movement;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;

public class Flight extends Module {

    private float speed = 1f;
    private float speedMultiplier = 2f;
    private int ticks = 0;

    public Flight() {
        super("Flight", "Allows you to fly in survival/adventure mode", InputUtil.GLFW_KEY_F);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.airStrafingSpeed = 0.02f;
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.getAbilities().flying = false;
        player.airStrafingSpeed = speed;
        if (player.isSprinting()) {
            player.airStrafingSpeed *= speedMultiplier;
        }

        player.setVelocity(0, 0, 0);
        if (player.input.jumping) {
            player.setVelocity(player.getVelocity().x, player.airStrafingSpeed, player.getVelocity().z);
        } else if (player.input.sneaking) {
            player.airStrafingSpeed = 0.85f;
            player.setVelocity(player.getVelocity().x, -player.airStrafingSpeed, player.getVelocity().z);
        }
        // prevent kick
        ticks++;
        if (ticks % 80 == 0) {
            if (player.isSneaking()) {
                ticks = 2;
            } else {
                player.setVelocity(player.getVelocity().x, -0.07, player.getVelocity().z);
            }
        } else if (ticks % 80 == 1) {
            player.setVelocity(player.getVelocity().x, 0.07, player.getVelocity().z);
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
}

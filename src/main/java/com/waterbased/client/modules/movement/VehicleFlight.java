package com.waterbased.client.modules.movement;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;

public class VehicleFlight extends Module {
    private double speed = 1f;
    private double forwardSpeedMultiplier = 0.60f;

    private int descendKey = InputUtil.GLFW_KEY_LEFT_ALT;

    public VehicleFlight() {
        super("VehicleFlight", "Allows you to fly in vehicles");
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        Entity vehicle = player.getVehicle();
        if (vehicle == null) return;
        vehicle.setVelocity(0, 0, 0);
        double ySpeed = vehicle.getVelocity().y;

        // Flight
        if (this.isPressingDescendKey()) {
            ySpeed = -speed;
        } else if (player.input.jumping) {
            ySpeed = speed;
        }

        if (!vehicle.isOnGround() && vehicle instanceof BoatEntity) {
            // boats are constantly falling with 0.04
            vehicle.addVelocity(0, 0.04, 0);
        }

        // forward speed
        double direction = Math.toRadians(vehicle.getYaw());
        double forwardSpeed = speed * forwardSpeedMultiplier;
        double zSpeed = 0;
        double xSpeed = 0;
        if (player.input.pressingBack) {
            zSpeed *= -1;
            xSpeed *= -1;
        } else if (player.input.pressingForward) {
            xSpeed = -Math.sin(direction) * forwardSpeed;
            zSpeed = Math.cos(direction) * forwardSpeed;
        }

        vehicle.setVelocity(xSpeed, ySpeed, zSpeed);

        //  vehicle.getVelocity().lengthSquared() with 2 decimals
        Client.LOGGER.info("Speed: " + Math.round(vehicle.getVelocity().lengthSquared() * 100.0) / 100.0);
    }

    private boolean isPressingDescendKey() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), descendKey);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        if (speed < 0) throw new IllegalArgumentException("Speed cannot be negative");
        this.speed = speed;
    }

    public double getForwardSpeedMultiplier() {
        return forwardSpeedMultiplier;
    }

    public void setForwardSpeedMultiplier(float forwardSpeedMultiplier) {
        if (forwardSpeedMultiplier < 0) {
            throw new IllegalArgumentException("Forward speed multiplier cannot be negative");
        }
        this.forwardSpeedMultiplier = forwardSpeedMultiplier;
    }

    public int getDescendKey() {
        return descendKey;
    }

    public void setDescendKey(int descendKey) {
        this.descendKey = descendKey;
    }
}

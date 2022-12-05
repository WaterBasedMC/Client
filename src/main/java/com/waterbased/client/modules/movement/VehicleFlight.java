package com.waterbased.client.modules.movement;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.vehicle.BoatEntity;

import java.util.HashMap;
import java.util.Map;

public class VehicleFlight extends Module {
    private double speed = 1f;
    private double forwardSpeedMultiplier = 0.64f;
    public static final Map<Class<? extends Entity>, Double> ANTI_FALL_VALUES = new HashMap<>(
            Map.of(
                    BoatEntity.class, 0.04d,
                    AbstractHorseEntity.class, 0.0d
            )
    );
    public VehicleFlight() {
        super("VehicleFlight", "Allows you to fly in vehicles");
    }

    @Override
    public void onEnable() {


    }

    @Override
    public void onDisable() {

    }

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
        if (MinecraftClient.getInstance().options.sprintKey.isPressed()) {
            ySpeed = -speed;
        } else if (player.input.jumping) {
            ySpeed = speed;
        }

        vehicle.setVelocity(vehicle.getVelocity().x, ySpeed, vehicle.getVelocity().z);
        // if not on grond, move upwards to prevent falling
        if (!vehicle.isOnGround()) {
            double antiFallValue = 0.03;
            for (Map.Entry<Class<? extends Entity>, Double> entry : ANTI_FALL_VALUES.entrySet()) {
                if (entry.getKey().isInstance(vehicle)) {
                    antiFallValue = entry.getValue();
                    break;
                }
            }
            vehicle.addVelocity(0, antiFallValue, 0);
        }

        // forward speed
        double direction = Math.toRadians(vehicle.getYaw());
        double forwardSpeed = speed * forwardSpeedMultiplier;
        double xSpeed = -Math.sin(direction) * forwardSpeed;
        double zSpeed = Math.cos(direction) * forwardSpeed;
        if (player.input.pressingForward) {
            vehicle.setVelocity(xSpeed, vehicle.getVelocity().y, zSpeed);
        } else if (player.input.pressingBack) {
            vehicle.setVelocity(-xSpeed, vehicle.getVelocity().y, -zSpeed);
        }
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
        if (forwardSpeedMultiplier < 0) throw new IllegalArgumentException("Forward speed multiplier cannot be negative");
        this.forwardSpeedMultiplier = forwardSpeedMultiplier;
    }

}

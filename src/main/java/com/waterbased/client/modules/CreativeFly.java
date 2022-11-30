package com.waterbased.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import static com.waterbased.client.Client.MODULE_MANAGER;

public class CreativeFly extends Module {

    public CreativeFly() {
        super("Creative Fly", "Allows you to fly in creative mode.");
    }

    private int ticks = 0;

    @Override
    public void onEnable() {
        this.ticks = 0;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.getAbilities().flying = false;
        player.airStrafingSpeed = 1f;
        if (player.isSprinting()) {
            player.airStrafingSpeed *= 2f;
        }

        player.setVelocity(0, 0, 0);
        if (player.input.jumping) {
            player.setVelocity(player.getVelocity().x, player.airStrafingSpeed, player.getVelocity().z);
        } else if (player.input.sneaking) {
            player.setVelocity(player.getVelocity().x, -player.airStrafingSpeed, player.getVelocity().z);
        }
        // prevent kick
        ticks++;
        if (ticks % 80 == 0) {
            player.setVelocity(player.getVelocity().x, -0.07, player.getVelocity().z);
        } else if (ticks % 80 == 1) {
            player.setVelocity(player.getVelocity().x, 0.07, player.getVelocity().z);
        }
    }

    @Override
    public void onKey(int key) {
        if (key == 72) {
            MODULE_MANAGER.getModule(CreativeFly.class).toggleState();
        }
    }
}

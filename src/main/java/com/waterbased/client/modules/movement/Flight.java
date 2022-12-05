package com.waterbased.client.modules.movement;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import com.waterbased.client.modules.player.NoFall;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;

public class Flight extends Module {

    private int ticks = 0;
    private boolean oldNoFallState = false;

    public Flight() {
        super("Flight", "Allows you to fly in survival/adventure mode", InputUtil.GLFW_KEY_F);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
        NoFall nf = (NoFall) Client.INSTANCE.MODULE_MANAGER.getModule(NoFall.class);
        this.oldNoFallState = nf.isEnabled();
        nf.forceState(true);
    }

    @Override
    public void onDisable() {
        NoFall nf = (NoFall) Client.INSTANCE.MODULE_MANAGER.getModule(NoFall.class);
        nf.forceState(this.oldNoFallState);
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
}

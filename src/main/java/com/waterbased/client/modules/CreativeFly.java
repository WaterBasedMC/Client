package com.waterbased.client.modules;

import com.waterbased.client.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class CreativeFly extends Module {

    public CreativeFly() {
        super("Creative Fly", "Allows you to fly in creative mode.");
    }
    private int ticks = 0;

    @Override
    public void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            Client.LOGGER.info("Creative Fly enabled.");
            player.getAbilities().flying = true;
            player.getAbilities().allowFlying = true;
            player.sendAbilitiesUpdate();
        } else {
            Client.LOGGER.error("Player is null.");
        }
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            Client.LOGGER.info("Creative Fly disabled.");
            player.getAbilities().flying = false;
            player.getAbilities().allowFlying = false;
            player.sendAbilitiesUpdate();
        } else {
            Client.LOGGER.error("Player is null.");
        }
    }

    @Override
    public void onWorldJoin() {

    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.getAbilities().flying = false;
        player.airStrafingSpeed = 0.02f;

        player.setVelocity(0, 0, 0);
        if (player.input.jumping) {
            player.setVelocity(player.getVelocity().x, 0.5, player.getVelocity().z);
        } else if (player.input.sneaking) {
            player.setVelocity(player.getVelocity().x, -0.5, player.getVelocity().z);
        }
        // prevent kick
        if (ticks++ % 80 == 0) {
            player.setVelocity(player.getVelocity().x, -0.07, player.getVelocity().z);
        }


    }

    @Override
    public void onKey(int key) {
    }
}

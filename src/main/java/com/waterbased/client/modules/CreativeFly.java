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
        this.ticks = 0;
    }

    @Override
    public void onDisable() {
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
        player.airStrafingSpeed = 2f;

        player.setVelocity(0, 0, 0);
        if (player.input.jumping) {
            player.setVelocity(player.getVelocity().x, 0.5, player.getVelocity().z);
        } else if (player.input.sneaking) {
            player.setVelocity(player.getVelocity().x, -0.5, player.getVelocity().z);
        }
        // prevent kick
        ticks++;
        if (ticks % 80 == 0) {
            player.setVelocity(player.getVelocity().x, -0.07, player.getVelocity().z);
        } else if (ticks % 80 == 1) {
            player.setVelocity(player.getVelocity().x, 0.07, player.getVelocity().z);
        }

        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(player.isOnGround()));

    }

    @Override
    public void onKey(int key) {
    }
}

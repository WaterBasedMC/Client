package com.waterbased.client.modules;

import com.waterbased.client.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage");
    }

    @Override
    public void onEnable() {
        // called when the module is enabled
    }

    @Override
    public void onDisable() {
        // called when the module is disabled
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (player.fallDistance <= (player.isFallFlying() ? 1 : 2))
            return;

        if (player.isFallFlying() && player.isSneaking()
                && !(player.getVelocity().y < -0.5))
            return;

        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    }

    @Override
    public void onKey(int key) {
        if (key == 74) {
            Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
        }
    }
}

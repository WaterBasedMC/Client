package com.waterbased.client.modules.player;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage", InputUtil.GLFW_KEY_N);
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
        if (player.fallDistance <= (player.isFallFlying() ? 1 : 2)) {
            return;
        }

        if (player.isFallFlying() && player.isSneaking()
                && !(player.getVelocity().y < -0.5)) {
            return;
        }

        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    }
}

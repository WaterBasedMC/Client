package com.waterbased.client.modules;

import com.waterbased.client.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.Box;

public class FreeCam extends Module {
    public boolean flying = false;

    public FreeCam() {
        super("FreeCam", "Allows you to fly around and interact with the world without moving your player");
    }

    @Override
    public void onEnable() {
//        e = new OtherClientPlayerEntity(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getGameProfile(), MinecraftClient.getInstance().player.getPublicKey());
//        e.setGlowing(true);
//        e.updatePositionAndAngles(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ(), MinecraftClient.getInstance().player.getYaw(), MinecraftClient.getInstance().player.getPitch());
//        e.resetPosition();
//        MinecraftClient.getInstance().world.addEntity(e.getId(), e);
        ZombieEntity e = new ZombieEntity(MinecraftClient.getInstance().world);
        e.updatePositionAndAngles(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ(), MinecraftClient.getInstance().player.getYaw(), MinecraftClient.getInstance().player.getPitch());
        MinecraftClient.getInstance().world.addEntity(e.getId(), e);

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        player.getAbilities().flying = flying;
        player.copyPositionAndRotation(e);
        MinecraftClient.getInstance().world.removeEntity(e.getId(), Entity.RemovalReason.UNLOADED_WITH_PLAYER);
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().player.setBoundingBox(new Box(MinecraftClient.getInstance().player.getPos(), MinecraftClient.getInstance().player.getPos()));
        MinecraftClient.getInstance().player.getAbilities().flying = false;
    }

    @Override
    public void onKey(int key) {
        if (key == 73) {
            Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
        }
    }
}

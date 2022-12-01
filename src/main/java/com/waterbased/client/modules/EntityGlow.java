package com.waterbased.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import static com.waterbased.client.Client.MODULE_MANAGER;

public class EntityGlow extends Module {
    public EntityGlow() {
        super("EntityGlow", "Lights up entities");
    }

    @Override
    public void onEnable() {
        // see mixin\ClientWorldMixin
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;
        world.getEntities().forEach(entity -> entity.setGlowing(true));
    }

    @Override
    public void onDisable() {
        // see mixin\ClientWorldMixin
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;
        world.getEntities().forEach(entity -> entity.setGlowing(false));
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onKey(int key) {
        if (key == 67) {
            MODULE_MANAGER.getModule(this.getClass()).toggleState();
        }
    }
}

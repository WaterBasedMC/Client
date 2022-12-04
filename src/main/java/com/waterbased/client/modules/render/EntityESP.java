package com.waterbased.client.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;

import java.util.HashSet;
import java.util.List;

public class EntityESP extends Module {

    private HashSet<Integer> glowingEntities = new HashSet<>();

    public EntityESP() {
        super("EntityESP", "Make all living entities glow", InputUtil.GLFW_KEY_MINUS);
    }

    @Override
    public void onEnable() {
        // see mixin\ClientWorldMixin
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        world.getEntities().forEach(entity -> entity.setGlowing(true));
        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if (entity instanceof LivingEntity le) {
                if (le.isGlowing()) {
                    this.glowingEntities.add(le.getId());
                }
                this.setEntityGlow(le, true);
            }
        }
    }

    @Override
    public void onDisable() {
        // see mixin\ClientWorldMixin
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if (entity instanceof LivingEntity le) {
                if (!this.glowingEntities.contains(le.getId())) {
                    this.setEntityGlow(le, false);
                }
            }
        }
        this.glowingEntities.clear();
    }

    @Override
    public void onRenderLevel() {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        buffer.vertex(10, 4, 0).next();
        buffer.vertex(10, 20, 0).next();
        buffer.color(0x80FF0000);
        tessellator.draw();

    }

    @Override
    public void onKey() {
        Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
    }

    public void onEntitySpawn(Entity entity) {
        if (entity instanceof LivingEntity le) {
            if (entity.isGlowing()) {
                this.glowingEntities.add(le.getId());
            }
            this.setEntityGlow(le, true);
        }
    }

    public void onEntityMetadataUpdate(Entity entity) {
        if (entity instanceof LivingEntity le) {
            if (entity.isGlowing()) {
                this.glowingEntities.add(le.getId());
            } else {
                this.glowingEntities.remove(le.getId());
            }
            this.setEntityGlow(le, true);
        }
    }

    public void setEntityGlow(LivingEntity entity, boolean glow) {
        if (entity.getDataTracker().getAllEntries() == null)
            return;

        for (DataTracker.Entry<?> entry : entity.getDataTracker().getAllEntries()) {
            if (entry.getData().getId() == 0) {
                DataTracker.Entry<Byte> entry1 = (DataTracker.Entry<Byte>) entry;
                byte value = (Byte) entry.get();

                if (glow) {
                    entry1.set((byte) (value | 0x40));
                } else {
                    entry1.set((byte) (value & (~0x40)));
                }

                entity.getDataTracker().writeUpdatedEntries(List.of(entry1));
            }
        }
    }

}

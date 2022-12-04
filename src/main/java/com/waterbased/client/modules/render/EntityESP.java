package com.waterbased.client.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;

import java.util.HashSet;
import java.util.List;

public class EntityESP extends Module {

    private HashSet<Integer> glowingEntities = new HashSet<>();

    public EntityESP() {
        super("EntityESP", "Make all living entities glow", InputUtil.GLFW_KEY_MINUS);
    }

    @Override
    public void onEnable() {
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
    public void onRenderLevel(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, double cameraX, double cameraY, double cameraZ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.world == null) return;
        if(client.player == null) return;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(1.0f, 0.0f, 0.0f, 0.75f);
        RenderSystem.disableTexture();
        client.world.getEntities().forEach(entity -> {
            if(entity.getId() == client.player.getId()) return;
            DebugRenderer.drawBox(entity.getBoundingBox().offset(-cameraX, -cameraY, -cameraZ), 1.0f, 0.0f, 0.0f, 0.75f);
        });
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public void onEntitySpawn(Entity entity) {
        if(true) return;
        if (entity instanceof LivingEntity le) {
            if (entity.isGlowing()) {
                this.glowingEntities.add(le.getId());
            }
            this.setEntityGlow(le, true);
        }
    }

    public void onEntityMetadataUpdate(Entity entity) {
        if(true) return;
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

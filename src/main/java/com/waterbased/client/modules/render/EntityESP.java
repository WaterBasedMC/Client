package com.waterbased.client.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.waterbased.client.modules.Module;
import com.waterbased.client.util.UtilEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.HashSet;

public class EntityESP extends Module {

    private final HashSet<Integer> glowingEntities = new HashSet<>();

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
                UtilEntity.setEntityGlow(le, true);
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
                    UtilEntity.setEntityGlow(le, false);
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
            UtilEntity.setEntityGlow(le, true);
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
            UtilEntity.setEntityGlow(le, true);
        }
    }

}

package com.waterbased.client.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
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

        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();

        RenderSystem.disableTexture();

        BufferBuilder bufferBuilder = tessellator.getBuffer();

        RenderSystem.lineWidth(2.0f);
        bufferBuilder.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(0 - cameraX, 5 - cameraY, 0 - cameraZ).color(0x80FF0000).next();
        bufferBuilder.vertex(0 - cameraX, 5 - cameraY, 0 - cameraZ).color(0x80FF0000).next();
        bufferBuilder.vertex(5 - cameraX, 5 - cameraY, 0 - cameraZ).color(0x80FF0000).next();
        bufferBuilder.vertex(0 - cameraX, 5 - cameraY, 5 - cameraZ).color(0x80FF0000).next();
        tessellator.draw();

        RenderSystem.enableBlend();
        RenderSystem.enableTexture();

        System.out.println("Render");

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

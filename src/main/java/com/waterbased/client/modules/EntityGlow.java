package com.waterbased.client.modules;

import com.waterbased.client.Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.HashSet;
import java.util.List;

public class EntityGlow extends Module {
    public EntityGlow() {
        super("EntityGlow", "Lights up entities", InputUtil.GLFW_KEY_MINUS);
    }

    private HashSet<Integer> glowingEntities = new HashSet<>();

    @Override
    public void onEnable() {
        // see mixin\ClientWorldMixin
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        world.getEntities().forEach(entity -> entity.setGlowing(true));
        for(Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if(entity instanceof LivingEntity le) {
                if(le.isGlowing()) {
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

        for(Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if(entity instanceof LivingEntity le) {
                if(!this.glowingEntities.contains(le.getId())) {
                    this.setEntityGlow(le, false);
                }
            }
        }
        this.glowingEntities.clear();
    }

    @Override
    public void onKey() {
        Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
    }

    public void onEntitySpawn(Entity entity) {
        if(entity instanceof LivingEntity le) {
            if(entity.isGlowing()) {
                this.glowingEntities.add(le.getId());
            }
            this.setEntityGlow(le, true);
        }
    }

    public void onEntityMetadataUpdate(Entity entity) {
        if(entity instanceof LivingEntity le) {
            if(entity.isGlowing()) {
                this.glowingEntities.add(le.getId());
            } else {
                this.glowingEntities.remove(le.getId());
            }
            this.setEntityGlow(le, true);
        }
    }

    public void setEntityGlow(LivingEntity entity, boolean glow) {
        if(entity.getDataTracker().getAllEntries() == null)
            return;

        for(DataTracker.Entry<?> entry : entity.getDataTracker().getAllEntries()) {
            if(entry.getData().getId() == 0) {
                DataTracker.Entry<Byte> entry1 = (DataTracker.Entry<Byte>) entry;
                byte value = (Byte)entry.get();

                if(glow) {
                    entry1.set((byte) (value | 0x40));
                } else {
                    entry1.set((byte) (value & (~0x40)));
                }

                entity.getDataTracker().writeUpdatedEntries(List.of(entry1));
            }
        }
    }

}

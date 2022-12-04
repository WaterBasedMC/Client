package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.render.EntityESP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    // Glow Entities
    @Inject(at = @At("HEAD"), method = "addEntity")
    public void addEntity(int id, Entity entity, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule(EntityESP.class) != null &&
                ModuleManager.INSTANCE.getModule(EntityESP.class).isEnabled()) {
            Client.LOGGER.info("EntityGlow: adding entity");
            if (entity instanceof LivingEntity) {
                if (entity != MinecraftClient.getInstance().player) {
                    Client.LOGGER.info("Glowing entity: " + entity.getName().getString());
                    entity.setGlowing(true);
                    Client.LOGGER.info("Success? " + entity.isGlowing() + " -- " + entity.isGlowingLocal());
                }
            }
        }
    }
}

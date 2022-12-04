package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.EntityGlow;
import com.waterbased.client.ui.HUDInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {


    @Inject(at = @At("TAIL"), method = "onEntitySpawn", cancellable = true)
    void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {

        if(Client.INSTANCE.MODULE_MANAGER.getModule(EntityGlow.class).isEnabled()) {
            Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.getId());
            Client.INSTANCE.MODULE_MANAGER.getModule(EntityGlow.class).onEntitySpawn(entity);
        }

    }

    @Inject(at = @At("TAIL"), method = "onEntityTrackerUpdate", cancellable = true)
    void onEntityStatusEffect(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {

        if(Client.INSTANCE.MODULE_MANAGER.getModule(EntityGlow.class).isEnabled()) {
            Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.id());
            Client.INSTANCE.MODULE_MANAGER.getModule(EntityGlow.class).onEntityMetadataUpdate(entity);
        }

    }

    @Inject(at = @At("TAIL"), method = "onGameJoin", cancellable = true)
    void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        Client.INSTANCE.MODULE_MANAGER.getModule(HUDInfo.class).forceState(true);
    }

}

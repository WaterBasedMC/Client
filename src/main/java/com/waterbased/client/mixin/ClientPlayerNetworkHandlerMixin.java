package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.movement.SpectatorCam;
import com.waterbased.client.modules.render.EntityESP;
import com.waterbased.client.modules.utilities.PlayerAlert;
import com.waterbased.client.ui.HUDInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {


    @Inject(at = @At("TAIL"), method = "onEntitySpawn")
    void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {

        /* MODULE: EntityESP */
        {
            if (Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).isEnabled()) {
                Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.getId());
                Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).onEntitySpawn(entity);
            }
        }

    }

    @Inject(at = @At("TAIL"), method = "onEntityTrackerUpdate")
    void onEntityStatusEffect(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {

        if(Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).isEnabled()) {
            Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.id());
            Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).onEntityMetadataUpdate(entity);
        }

    }

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        Client.INSTANCE.MODULE_MANAGER.getModule(HUDInfo.class).forceState(true);
    }

    @Inject(at = @At("TAIL"), method = "onPlayerSpawn")
    void onPlayerSpawn(PlayerSpawnS2CPacket packet, CallbackInfo ci) {
        /* MODULE: PlayerAlert */
        {
            World world = MinecraftClient.getInstance().world;
            if (world != null && Client.INSTANCE.MODULE_MANAGER.getModule(PlayerAlert.class).isEnabled()) {
                OtherClientPlayerEntity otherPlayer = (OtherClientPlayerEntity) world.getEntityById(packet.getId());
                if (otherPlayer != null) {
                    Client.INSTANCE.MODULE_MANAGER.getModule(PlayerAlert.class).onNewPlayer(otherPlayer);
                } else {
                    Client.LOGGER.warning("PlayerSpawnS2CPacket contained invalid entity ID " + packet.getId());
                }
            }
        }
    }

    @ModifyVariable(at = @At("STORE"), method = "onPlayerPositionLook")
    private PlayerEntity onPlayerPositionLook(PlayerEntity playerEntity) {
        SpectatorCam specCam = Client.INSTANCE.MODULE_MANAGER.getModule(SpectatorCam.class);
        if (specCam.isEnabled() && specCam.getClone() != null) {
            return specCam.getClone();
        }
        return playerEntity;
    }

}

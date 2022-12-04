package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.EntityESP;
import com.waterbased.client.modules.PlayerAlert;
import com.waterbased.client.ui.HUDInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {


    @Inject(at = @At("TAIL"), method = "onEntitySpawn", cancellable = true)
    void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {

        /* MODULE: EntityESP */
        {
            if(Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).isEnabled()) {
                Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.getId());
                Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).onEntitySpawn(entity);
            }
        }


        /* MODULE: PlayerAlert */
        {
            World world = MinecraftClient.getInstance().world;
            if (world != null && Client.INSTANCE.MODULE_MANAGER.getModule(PlayerAlert.class).isEnabled()) {
                OtherClientPlayerEntity otherPlayer = (OtherClientPlayerEntity) world.getEntityById(packet.getId());
                if (otherPlayer != null) {
                    Text name = otherPlayer.getDisplayName();
                    assert MinecraftClient.getInstance().player != null;
                    float distanceToPlayer = MinecraftClient.getInstance().player.distanceTo(otherPlayer);
                    distanceToPlayer = Math.round(distanceToPlayer * 100.0f) / 100.0f;
                    MinecraftClient.getInstance().inGameHud.getChatHud()
                            .addMessage(Text.of(String.format(PlayerAlert.MESSAGE_TEMPLATE.getString(), otherPlayer.getDisplayName()
                                    .getString(), distanceToPlayer)));
                } else {
                    Client.LOGGER.warning("PlayerSpawnS2CPacket contained invalid entity ID " + packet.getId());
                }
            }
        }


    }

    @Inject(at = @At("TAIL"), method = "onEntityTrackerUpdate", cancellable = true)
    void onEntityStatusEffect(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {

        if(Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).isEnabled()) {
            Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.id());
            Client.INSTANCE.MODULE_MANAGER.getModule(EntityESP.class).onEntityMetadataUpdate(entity);
        }

    }

    @Inject(at = @At("TAIL"), method = "onGameJoin", cancellable = true)
    void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        Client.INSTANCE.MODULE_MANAGER.getModule(HUDInfo.class).forceState(true);
    }

    @Inject(at = @At("TAIL"), method = "onPlayerSpawn")
    void onEntitySpawn(PlayerSpawnS2CPacket packet, CallbackInfo ci) {

    }

}

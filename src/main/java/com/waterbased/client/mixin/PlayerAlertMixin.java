package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.PlayerAlert;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayerAlertMixin {

    @Inject(at = @At("TAIL"), method = "onPlayerSpawn")
    private void onEntitySpawn(PlayerSpawnS2CPacket packet, CallbackInfo ci) {
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

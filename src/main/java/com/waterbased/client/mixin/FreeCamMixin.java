package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.FreeCam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FreeCamMixin {
    FreeCam freeCam = (FreeCam) Client.INSTANCE.MODULE_MANAGER.getModule(FreeCam.class);

    @Inject(at = @At("HEAD"), method = "pushOutOfBlocks", cancellable = true)
    void pushOutOfBlocks(CallbackInfo ci) {
        if (freeCam.isEnabled()) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "sendMovementPackets", cancellable = true)
    void sendMovementPackets(CallbackInfo ci) {
        if (freeCam.isEnabled()) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    void tick(CallbackInfo ci) {
        if (freeCam.isEnabled()) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            player.setBoundingBox(new Box(player.getPos(), player.getPos()));
            player.getAbilities().flying = true;
        }
    }
}

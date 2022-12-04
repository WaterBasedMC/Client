package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.movement.SpectatorCam;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FreeCamMixin {
    SpectatorCam spectatorCam = (SpectatorCam) Client.INSTANCE.MODULE_MANAGER.getModule(SpectatorCam.class);

    // TODO: Can be deleted

    @Inject(at = @At("HEAD"), method = "pushOutOfBlocks", cancellable = true)
    void pushOutOfBlocks(CallbackInfo ci) {
        if (spectatorCam.isEnabled()) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "sendMovementPackets", cancellable = true)
    void sendMovementPackets(CallbackInfo ci) {
        if (spectatorCam.isEnabled()) ci.cancel();
    }

}

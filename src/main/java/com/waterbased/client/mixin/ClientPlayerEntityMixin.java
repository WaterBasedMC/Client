package com.waterbased.client.mixin;


import com.waterbased.client.Client;
import com.waterbased.client.modules.FreeCam;
import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.NoSlowDown;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), method = "tickMovement()V")
    public boolean isUsingItem(ClientPlayerEntity clientPlayerEntity) {
        if (ModuleManager.INSTANCE.getModule(NoSlowDown.class) != null &&
                ModuleManager.INSTANCE.getModule(NoSlowDown.class).isEnabled()) {
            return false;
        }
        return clientPlayerEntity.isUsingItem();
    }

    FreeCam freeCam = (FreeCam) Client.MODULE_MANAGER.getModule(FreeCam.class);

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

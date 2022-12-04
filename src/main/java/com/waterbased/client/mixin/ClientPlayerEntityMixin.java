package com.waterbased.client.mixin;


import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.movement.NoSlowDown;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

}

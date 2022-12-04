package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.movement.NoSlowDown;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(EntityAttributeInstance.class)
public class EntityAttributeInstanceMixin {

    @Inject(at = @At("HEAD"), method = "addModifier", cancellable = true)
    private void addModifier(EntityAttributeModifier modifier, CallbackInfo ci) {
        if (Client.INSTANCE.MODULE_MANAGER.getModule(NoSlowDown.class).isEnabled()) {
            if (modifier.getId().equals(UUID.fromString(NoSlowDown.SLOWNESS_UUID))) {
                ci.cancel();
            }
        }
    }
}

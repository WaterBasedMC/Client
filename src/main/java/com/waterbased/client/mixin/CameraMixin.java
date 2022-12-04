package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.render.ClearSight;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    private void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if(Client.INSTANCE.MODULE_MANAGER.getModule(ClearSight.class).isEnabled()) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        }
    }

}

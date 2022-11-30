package com.waterbased.client.mixin;

import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.NoSlowDown;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class SlowdownMixin {

    @Inject(at = @At("HEAD"), method = "getVelocityMultiplier()F", cancellable = true)
    public void getVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (ModuleManager.INSTANCE.getModule(NoSlowDown.class) != null &&
                ModuleManager.INSTANCE.getModule(NoSlowDown.class).isEnabled()) {
            if (cir.getReturnValueF() < 1) {
                cir.setReturnValue(1f);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getSlipperiness()F", cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (ModuleManager.INSTANCE.getModule(NoSlowDown.class) != null &&
                ModuleManager.INSTANCE.getModule(NoSlowDown.class).isEnabled()) {
            cir.setReturnValue(0.6f);
        }
    }
}

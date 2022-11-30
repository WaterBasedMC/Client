package com.waterbased.client.mixin;

import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.NoSlowDown;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "slowMovement", cancellable = true)
    public void slowMovement(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule(NoSlowDown.class) != null &&
                ModuleManager.INSTANCE.getModule(NoSlowDown.class).isEnabled()) {
            ci.cancel();
        }
    }
}

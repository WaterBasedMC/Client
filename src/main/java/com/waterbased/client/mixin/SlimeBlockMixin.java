package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.movement.BouncySlime;
import com.waterbased.client.modules.movement.NoSlowDown;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

    @Inject(at = @At("HEAD"), method = "onSteppedOn", cancellable = true)
    private void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (Client.INSTANCE.MODULE_MANAGER.getModule(NoSlowDown.class).isEnabled()) {
            ci.cancel();
        }
    }

    @ModifyConstant(method = "bounce", constant = @Constant(doubleValue = 1.0D))
    private double modifyBounciness(double original) {
        BouncySlime bouncySlime = Client.INSTANCE.MODULE_MANAGER.getModule(BouncySlime.class);
        if (bouncySlime.isEnabled()) {
            return bouncySlime.getBounciness();
        }
        return original;
    }
}

package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        Client.INSTANCE.onRenderLevel();
    }

}

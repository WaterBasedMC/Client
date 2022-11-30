package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    // on key press
    @Inject(at = @At("HEAD"), method = "onKey")
    public void onKey(long window, int key, int scancode, int i, int j, CallbackInfo ci) {
        Client.INSTANCE.onKey(key);
    }
}

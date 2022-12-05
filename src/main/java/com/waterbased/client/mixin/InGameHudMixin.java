package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.util.UtilUI;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow
    private int heldItemTooltipFade;

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Client.INSTANCE.onRenderInGameHUD(matrices, tickDelta);
    }

    /* UtilUI: ItemNameDisplay */
    @ModifyVariable(at = @At("STORE"), method = "renderHeldItemTooltip")
    private MutableText renderHeldItemTooltipChangeMutableText(MutableText mutableText) {
        return UtilUI.displayItemNames != null ? UtilUI.displayItemNames : mutableText;
    }

    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;heldItemTooltipFade:I", opcode = Opcodes.GETFIELD))
    private int renderHeldItemTooltipChangeTooltipFade(InGameHud inGameHud) {
        return UtilUI.displayItemNames != null ? UtilUI.OWN_HELD_ITEM_FADE_OUT : heldItemTooltipFade;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;getCurrentGameMode()Lnet/minecraft/world/GameMode;"))
    private GameMode renderChangeGameMode(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        return UtilUI.displayItemNames != null ? GameMode.SURVIVAL : clientPlayerInteractionManager.getCurrentGameMode();
    }

    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", opcode = Opcodes.GETFIELD))
    private boolean renderHeldItemTooltipChangeMutableText(ItemStack itemStack) {
        if (UtilUI.displayItemNames != null) {
            return false;
        }
        return itemStack.isEmpty();
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"))
    private void renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
        if (UtilUI.displayItemNames != null && UtilUI.OWN_HELD_ITEM_FADE_OUT == 0) {
            UtilUI.displayItemNames = null;
            UtilUI.OWN_HELD_ITEM_FADE_OUT = 40;
        }
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (UtilUI.displayItemNames != null) {
            UtilUI.OWN_HELD_ITEM_FADE_OUT--;
        }
    }
}

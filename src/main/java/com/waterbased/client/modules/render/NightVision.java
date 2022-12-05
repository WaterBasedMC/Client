package com.waterbased.client.modules.render;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class NightVision extends Module {
    public NightVision() {
        super("NightVision", "Allows you to see in the dark", InputUtil.GLFW_KEY_COMMA);
    }

    @Override
    public void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        StatusEffectInstance nightVision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 99999,
                1, false, false
        );
        nightVision.setPermanent(true);
        player.addStatusEffect(nightVision);
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }

}

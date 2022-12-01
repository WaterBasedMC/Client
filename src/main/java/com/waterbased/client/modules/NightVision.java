package com.waterbased.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import static com.waterbased.client.Client.LOGGER;
import static com.waterbased.client.Client.MODULE_MANAGER;

public class NightVision extends Module {
    public NightVision() {
        super("NightVision", "Allows you to see in the dark");
    }

    @Override
    public void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        StatusEffectInstance nightVision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 99999,
                1, false, false);
        nightVision.setPermanent(true);
        player.addStatusEffect(nightVision);
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onKey(int key) {
        if (key == 77) { // M
            MODULE_MANAGER.getModule(this.getClass()).toggleState();
        }
    }
}

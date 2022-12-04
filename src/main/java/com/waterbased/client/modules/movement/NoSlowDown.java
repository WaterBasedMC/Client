package com.waterbased.client.modules.movement;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.minecraft.client.util.InputUtil;

public class NoSlowDown extends Module {

    public static final String SLOWNESS_UUID = "7107DE5E-7CE8-4030-940E-514C1F160890";

    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from slowing down, when using items or going through blocks", InputUtil.GLFW_KEY_B);
    }

    @Override
    public void onEnable() {
        // see mixin\SlowdownMixin
        // see mixin\PlayerEntityMixin\slowMovement
        // see mixin\ClientPlayerEntityMixin\isUsingItem
    }

    @Override
    public void onDisable() {
        // see mixin\SlowdownMixin
        // see mixin\PlayerEntityMixin\slowMovement
        // see mixin\ClientPlayerEntityMixin\isUsingItem
    }

    @Override
    public void onKey() {
        Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
    }
}

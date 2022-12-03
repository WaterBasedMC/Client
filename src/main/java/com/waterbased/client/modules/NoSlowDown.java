package com.waterbased.client.modules;

import com.waterbased.client.Client;
import net.minecraft.client.util.InputUtil;

public class NoSlowDown extends Module {

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

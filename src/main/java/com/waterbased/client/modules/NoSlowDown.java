package com.waterbased.client.modules;

import static com.waterbased.client.Client.MODULE_MANAGER;

public class NoSlowDown extends Module {

    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from slowing down, when using items or going through blocks");
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
    public void onTick() {
    }

    @Override
    public void onKey(int key) {
        if (key == 70) {
            MODULE_MANAGER.getModule(this.getClass()).toggleState();
        }
    }
}

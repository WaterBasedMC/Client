package com.waterbased.client.modules;

import static com.waterbased.client.Client.MODULE_MANAGER;

public class AntiCactus extends Module {

    public AntiCactus() {
        super("AntiCactus", "Prevents you from taking damage from cacti");
    }

    @Override
    public void onEnable() {
        // see mixin\CactusMixin
    }

    @Override
    public void onDisable() {
        // see mixin\CactusMixin
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onKey(int key) {
        if (key == 71) {
            MODULE_MANAGER.getModule(this.getClass()).toggleState();
        }
    }
}

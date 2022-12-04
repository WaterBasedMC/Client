package com.waterbased.client.modules.utilities;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;

public class AntiCactus extends Module {

    public AntiCactus() {
        super("AntiCactus", "Prevents you from taking damage from cacti", null);
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
    public void onKey() {
        Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
    }
}

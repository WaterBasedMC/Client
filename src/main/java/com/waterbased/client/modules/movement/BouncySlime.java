package com.waterbased.client.modules.movement;

import com.waterbased.client.modules.Module;

public class BouncySlime extends Module {

    public static double BOUNCINESS = 1.2d;

    public BouncySlime() {
        super("BouncySlime", "Makes Slime Blocks more bouncy");
    }

    @Override
    public void onEnable() {

    }

    public double getBounciness() {
        // old value: 1.0
        return BOUNCINESS;
    }

    @Override
    public void onDisable() {

    }
}

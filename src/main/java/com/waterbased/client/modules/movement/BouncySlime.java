package com.waterbased.client.modules.movement;

import com.waterbased.client.modules.Module;

public class BouncySlime extends Module {

    public void setBounciness(double newBounciness) {
        if (newBounciness < 0.0d) {
            throw new IllegalArgumentException("Bounciness cannot be less than 0.0");
        }
        this.bounciness = newBounciness;
    }

    private double bounciness = 1.4d;

    public BouncySlime() {
        super("BouncySlime", "Makes Slime Blocks more bouncy");
    }

    @Override
    public void onEnable() {
    }

    public double getBounciness() {
        // old value: 1.0
        // used it /mixin/SlimeBlockMixin.java
        return Math.max(0.0d, bounciness); // prevent crash on negative values
    }

    @Override
    public void onDisable() {

    }
}

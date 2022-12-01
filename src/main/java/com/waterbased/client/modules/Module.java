package com.waterbased.client.modules;

import com.waterbased.client.Client;

public abstract class Module {

    private final String name;
    private final String description;
    private boolean enabled;

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.enabled = false;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onTick();

    public abstract void onKey(int key);

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void toggleState() {
        this.enabled = !this.enabled;
        callActivationCallbacks();
    }

    public void forceState(boolean state) {
        this.enabled = state;
        callActivationCallbacks();
    }

    private void callActivationCallbacks() {
        Client.LOGGER.info(this.name + " is now " + (this.enabled ? "enabled" : "disabled"));
        if (this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Module) {
            return ((Module) obj).getName().equalsIgnoreCase(this.name);
        }
        return false;
    }
}

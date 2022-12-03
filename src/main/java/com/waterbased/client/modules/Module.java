package com.waterbased.client.modules;

import com.waterbased.client.Client;
import org.jetbrains.annotations.Nullable;

public abstract class Module {

    private final String name;
    private final String description;
    private boolean enabled;
    private Integer key;

    public Module(String name, String description) {
        this(name, description, null);
    }

    public Module(String name, String description, @Nullable Integer key) {
        this.name = name;
        this.description = description;
        this.enabled = false;
        this.key = key;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onTick() {}

    public abstract void onKey();

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public @Nullable Integer getKey() {
        return this.key;
    }

    public void setKey(@Nullable Integer key) {
        this.key = key;
    }

    public void toggleState() {
        this.enabled = !this.enabled;
        callActivationCallbacks();
    }

    public void forceState(boolean state) {
        if (this.enabled == state) return;
        this.enabled = state;
        callActivationCallbacks();
    }

    private void callActivationCallbacks() {
        Client.HUD.onModuleStateChange(this);
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

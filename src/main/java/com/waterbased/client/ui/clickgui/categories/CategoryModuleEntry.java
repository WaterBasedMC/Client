package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.minecraft.client.util.InputUtil;

public class CategoryModuleEntry {

    private final Class<? extends Module> moduleClass;

    private boolean wasToggled = false;
    private boolean wasBound = false;
    private boolean wasSettingsOpened = false;

    public CategoryModuleEntry(Class<? extends Module> moduleClass) {
        this.moduleClass = moduleClass;
    }

    public String getName() {
        return Client.INSTANCE.MODULE_MANAGER.getModule(moduleClass).getName();
    }

    public String getDescription() {
        return Client.INSTANCE.MODULE_MANAGER.getModule(moduleClass).getDescription();
    }

    public boolean isEnabled() {
        return Client.INSTANCE.MODULE_MANAGER.getModule(moduleClass).isEnabled();
    }

    public void toggle() {
        Client.INSTANCE.MODULE_MANAGER.getModule(moduleClass).toggleState();
    }

    public boolean wasToggled() {
        return this.wasToggled;
    }

    public void setWasToggled(boolean b) {
        this.wasToggled = b;
    }

    public boolean wasBound() {
        return wasBound;
    }

    public void setWasBound(boolean wasBound) {
        this.wasBound = wasBound;
    }

    public boolean wasSettingsOpened() {
        return wasSettingsOpened;
    }

    public void setWasSettingsOpened(boolean wasSettingsOpened) {
        this.wasSettingsOpened = wasSettingsOpened;
    }

    public String getBinding() {
        Integer key = Client.INSTANCE.MODULE_MANAGER.getModule(moduleClass).getKey();
        if(key != null){
            return InputUtil.fromKeyCode(key, 0).getLocalizedText().getString();
        } else {
            return "None";
        }
    }

}

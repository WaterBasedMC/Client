package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;

public class CategoryEntry {

    private final Class<? extends Module> moduleClass;

    public CategoryEntry(Class<? extends Module> moduleClass) {
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



}

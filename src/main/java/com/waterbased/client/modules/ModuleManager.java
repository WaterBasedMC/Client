package com.waterbased.client.modules;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();

    public List<Module> getModules() {
        return this.modules;
    }

    public Module getModule(String name) {
        for (Module module : this.modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module module : this.modules) {
            if (module.getClass().equals(clazz)) {
                return module;
            }
        }
        return null;
    }

    public void addModule(Module module) {
        this.modules.add(module);
    }

    public void removeModule(Module module) {
        this.modules.remove(module);
    }

    public void toggleModule(String name) {
        Module module = this.getModule(name);
        if (module != null) {
            module.toggleState();
        }
    }

}

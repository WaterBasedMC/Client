package com.waterbased.client;

import com.waterbased.client.modules.Module;
import com.waterbased.client.modules.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("waterbased-client");
    public static final Client INSTANCE = new Client();
    public static final ModuleManager MODULE_MANAGER = ModuleManager.INSTANCE;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        MODULE_MANAGER.addModule(new CreativeFly());
        MODULE_MANAGER.addModule(new NoFall());
        MODULE_MANAGER.addModule(new FreeCam());
        MODULE_MANAGER.addModule(new AntiCactus());
        MODULE_MANAGER.addModule(new NoSlowDown());
        MODULE_MANAGER.addModule(new EntityGlow());
        for (Module module : MODULE_MANAGER.getModules()) {
            LOGGER.info(module.getName() + " - " + module.getDescription());
        }
    }

    public void onTick() {
        MODULE_MANAGER.getModules().forEach(Module::onTick);
    }

    public void onKey(int key) {
        // ignores if menu is open
        if (MinecraftClient.getInstance().currentScreen == null) {
            MODULE_MANAGER.getModules().forEach(module -> module.onKey(key));
        }
    }
}

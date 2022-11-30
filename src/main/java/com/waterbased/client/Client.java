package com.waterbased.client;

import com.waterbased.client.modules.CreativeFly;
import com.waterbased.client.modules.Module;
import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.NoFall;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("waterbased-client");
	public static final Client INSTANCE = new Client();
	public static final ModuleManager MODULE_MANAGER = new ModuleManager();

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		MODULE_MANAGER.addModule(new CreativeFly());
		MODULE_MANAGER.addModule(new NoFall());
		for (Module module : MODULE_MANAGER.getModules()) {
			LOGGER.info(module.getName() + " - " + module.getDescription());
		}
	}

	public void onTick() {
		MODULE_MANAGER.getModules().forEach(Module::onTick);
	}

	public void onKey(int key) {
		MODULE_MANAGER.getModules().forEach(module -> module.onKey(key));
	}
}

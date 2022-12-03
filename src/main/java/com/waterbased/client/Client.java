package com.waterbased.client;

import com.waterbased.client.modules.Module;
import com.waterbased.client.modules.*;
import com.waterbased.client.ui.HUDInfo;
import com.waterbased.client.ui.SelectionGUI;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Client implements ModInitializer {

    public static final Client INSTANCE = new Client();
    public static final String MOD_NAME = "WaterBased";
    public static final String MOD_VERSION = "0.0.1b1";
    public static final Logger LOGGER = Logger.getLogger("WaterBased");
    public static final HUDInfo HUD = new HUDInfo();
    public static boolean LOG_TO_CHAT = true;
    public final ModuleManager MODULE_MANAGER = ModuleManager.INSTANCE;

    @Override
    public void onInitialize() {
        setupLogger();
        LOGGER.info("Hello Fabric world!");
        HUD.setupContent();
        MODULE_MANAGER.addModule(HUD);
        MODULE_MANAGER.addModule(new CreativeFly());
        MODULE_MANAGER.addModule(new NoFall());
        MODULE_MANAGER.addModule(new FreeCam());
        MODULE_MANAGER.addModule(new AntiCactus());
        MODULE_MANAGER.addModule(new NoSlowDown());
        MODULE_MANAGER.addModule(new EntityGlow());
        MODULE_MANAGER.addModule(new NightVision());
        for (Module module : MODULE_MANAGER.getModules()) {
            LOGGER.info(module.getName() + " - " + module.getDescription());
        }
        MODULE_MANAGER.sortModules(true, true);
    }

    private void setupLogger() {
        // LOGGER add handler to log to chat if possible
        LOGGER.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (!LOG_TO_CHAT) return;
                // writes to mc chat (green = info, yellow = warning, red = severe)
                if (MinecraftClient.getInstance().inGameHud != null) {
                    String color = record.getLevel() == Level.INFO ? "§a" : record.getLevel() == Level.WARNING ? "§eWarning: " : "§cError: ";
                    MinecraftClient.getInstance().inGameHud.getChatHud()
                            .addMessage(Text.of(color + record.getMessage()));
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
        LOGGER.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("WaterBased"); // Maybe not the best way to do this
                if (record.getLevel() == Level.INFO) {
                    logger.info(record.getMessage());
                } else if (record.getLevel() == Level.WARNING) {
                    logger.warn(record.getMessage());
                } else if (record.getLevel() == Level.SEVERE) {
                    logger.error(record.getMessage());
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });

        LOGGER.setUseParentHandlers(false);
    }

    public void onTick() {
        MODULE_MANAGER.getModules().forEach(Module::onTick);
    }

    public void onKey(int key) {
        // ignores if menu is open
        if (MinecraftClient.getInstance().currentScreen == null) {
            MODULE_MANAGER.getModules()
                    .stream()
                    .filter(module -> module.getKey() != null && module.getKey() == key)
                    .forEach(Module::onKey);
            if (key == 96) { // ^
                MinecraftClient.getInstance().setScreen(new CottonClientScreen(new SelectionGUI()));
            }
        }
    }
}

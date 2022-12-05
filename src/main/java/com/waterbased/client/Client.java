package com.waterbased.client;

import com.waterbased.client.modules.Module;
import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.movement.Flight;
import com.waterbased.client.modules.movement.NoSlowDown;
import com.waterbased.client.modules.movement.SpectatorCam;
import com.waterbased.client.modules.render.ClearSight;
import com.waterbased.client.modules.render.EntityESP;
import com.waterbased.client.modules.render.NightVision;
import com.waterbased.client.modules.utilities.AntiCactus;
import com.waterbased.client.modules.player.NoFall;
import com.waterbased.client.modules.utilities.PlayerAlert;
import com.waterbased.client.ui.HUDInfo;
import com.waterbased.client.ui.clickgui.ClickGUI;
import com.waterbased.client.util.ChatManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
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
    public final ChatManager chatManager = new ChatManager();

    public final ClickGUI clickGUI = new ClickGUI();

    @Override
    public void onInitialize() {
        setupLogger();
        LOGGER.info("Hello Fabric world!");
        HUD.setupContent();
        MODULE_MANAGER.addModule(HUD);
        MODULE_MANAGER.addModule(new Flight());
        MODULE_MANAGER.addModule(new NoFall());
        MODULE_MANAGER.addModule(new SpectatorCam());
        MODULE_MANAGER.addModule(new AntiCactus());
        MODULE_MANAGER.addModule(new NoSlowDown());
        MODULE_MANAGER.addModule(new EntityESP());
        MODULE_MANAGER.addModule(new NightVision());
        MODULE_MANAGER.addModule(new PlayerAlert());
        MODULE_MANAGER.addModule(new ClearSight());
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
        MODULE_MANAGER.getModules().stream().filter(Module::isEnabled).forEach(Module::onTick);
    }

    public void onRenderInGameHUD(MatrixStack matrices, float tickDelta) {
        MODULE_MANAGER.getModules().stream().filter(Module::isEnabled).forEach(m -> m.onRenderInGameHUD(matrices, tickDelta));
    }

    public void onRenderLevel(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, double cameraX, double cameraY, double cameraZ) {
        MODULE_MANAGER.getModules().stream().filter(Module::isEnabled).forEach(m -> m.onRenderLevel(matrices, immediate, cameraX, cameraY, cameraZ));
    }

    public void onKey(int key) {
        // ignores if menu is open
        if (MinecraftClient.getInstance().currentScreen == null) {
            MODULE_MANAGER.getModules()
                    .stream()
                    .filter(module -> module.getKey() != null && module.getKey() == key)
                    .forEach(Module::onKey);
            if (key == 96) { // ^
                MinecraftClient.getInstance().setScreen(clickGUI);
            }
        }
    }
}

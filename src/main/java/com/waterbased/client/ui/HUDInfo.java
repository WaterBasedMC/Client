package com.waterbased.client.ui;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import io.github.cottonmc.cotton.gui.client.CottonHud;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class HUDInfo extends Module {

    public static int X_OFFSET = 2;
    private final List<WLabel> widgets = new ArrayList<>();

    public HUDInfo() {
        super("HUD", "Displays information on the screen");
    }

    @Override
    public void onEnable() {
        widgets.forEach(CottonHud::add);
    }

    public void setupContent() {
        setupTitle();

        setupModules();
    }

    private void setupModules() {
        // Active modules
        List<Module> activeModules = Client.INSTANCE.MODULE_MANAGER.getModules().stream()
                .filter(module -> module.isEnabled() && !module.equals(Client.HUD)).toList();
        if (activeModules.size() > 0) {
            activeModules.forEach(this::addModuleToHud);
        }
    }

    private void addModuleToHud(Module module) {
        Text name = MutableText.of(new LiteralTextContent("- " + module.getName()))
                .styled(style -> style
                        .withColor(Formatting.GREEN)
                );
        WLabel moduleLabel = new WLabel(name);
        int offSet = widgets.size() * 10;
        moduleLabel.setLocation(X_OFFSET, offSet);
        this.widgets.add(moduleLabel);
        if (this.isEnabled()) {
            CottonHud.add(moduleLabel);
        }
    }

    private void setupTitle() {
        // Title
        Text title = MutableText.of(new LiteralTextContent(Client.MOD_NAME + " " + Client.MOD_VERSION))
                .styled(style -> style
                        .withColor(Formatting.BLUE)
                );
        WLabel titleLabel = new WLabel(title);
        titleLabel.setLocation(X_OFFSET, 2);
        widgets.add(titleLabel);
    }

    @Override
    public void onDisable() {
        widgets.forEach(CottonHud::remove);
    }

    public void onModuleStateChange(Module module) {
        if (module.equals(this)) return;
        reset();
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onKey(int key) {
        if (key == InputUtil.GLFW_KEY_KP_0) { // 0
            Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
        } else if (key == InputUtil.GLFW_KEY_KP_1) { // 1 // TODO: Just for hot reloading
            reset();
        }
    }

    private void reset() {
        onDisable();
        widgets.clear();
        setupContent();
        if (this.isEnabled()) {
            onEnable();
        }
    }
}

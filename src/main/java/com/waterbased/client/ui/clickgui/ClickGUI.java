package com.waterbased.client.ui.clickgui;

import com.waterbased.client.ui.clickgui.categories.CategoryMovement;
import com.waterbased.client.ui.clickgui.categories.CategoryRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.HashSet;

public class ClickGUI extends Screen {

    private boolean active;
    private final HashSet<ClickGUIDrawable> drawables = new HashSet<>();

    public ClickGUI() {
        super(Text.of("WaterBased ClickGUI"));
        drawables.add(new CategoryRender(100, 100));
        drawables.add(new CategoryMovement(200, 100));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawables.forEach(drawable -> drawable.render(matrices, this.textRenderer, mouseX, mouseY, this));

    }





}

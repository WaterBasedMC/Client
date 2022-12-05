package com.waterbased.client.ui.clickgui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface ClickGUIDrawable {

    void render(MatrixStack matrices, TextRenderer renderer, int mouseX, int mouseY, ClickGUI gui);

}

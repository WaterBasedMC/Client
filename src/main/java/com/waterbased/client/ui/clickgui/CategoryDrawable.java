package com.waterbased.client.ui.clickgui;

import com.waterbased.client.ui.clickgui.categories.CategoryModuleEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.List;

public abstract class CategoryDrawable implements ClickGUIDrawable {

    private static final int HEADER_HEIGHT = 16;
    private static final int ENTRY_HEIGHT = 12;
    private static final int ENTRY_PADDING = 2;

    private final String name;
    private int x, y;
    private int width, height;
    private int lastMouseX, lastMouseY;

    private int color = 0xCC2481AA;

    private boolean isHovered = false;

    private HashSet<CategoryModuleEntry> entries = new HashSet<>();

    protected CategoryDrawable(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected CategoryDrawable(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY, ClickGUI gui) {

        if (this.width < textRenderer.getWidth(this.name) + ENTRY_PADDING * 2 + 10) {
            this.width = textRenderer.getWidth(this.name) + ENTRY_PADDING * 2 + 10;
        }
        if (this.height < textRenderer.getWrappedLinesHeight(this.name, this.width) + 20) {
            this.height = textRenderer.getWrappedLinesHeight(this.name, this.width) + 20;
        }

        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + HEADER_HEIGHT) {
            this.isHovered = true;
        } else {
            this.isHovered = false;
        }

        boolean changed = false;
        for (CategoryModuleEntry entry : this.entries) {
            if (this.width < textRenderer.getWidth(entry.getName())) {
                this.width = textRenderer.getWidth(entry.getName());
                changed = true;
            }
        }
        if (changed) {
            this.width += ENTRY_PADDING * 2;
        }

        //DrawTitle
        drawTitle(matrices, textRenderer);
        drawEntries(matrices, textRenderer, mouseX, mouseY, gui);

        //Check if mouse is pressed
        if (GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            if (this.isHovered) {
                this.x += mouseX - this.lastMouseX;
                this.y += mouseY - this.lastMouseY;
            }
        }

        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }

    private void drawTitle(MatrixStack matrices, TextRenderer textRenderer) {
        DrawableHelper.fill(matrices, this.x, this.y, this.x + this.width, this.y + HEADER_HEIGHT, this.color);

        Text txt = Text.of(this.name);
        textRenderer.draw(matrices, txt, this.x + ENTRY_PADDING, y + ENTRY_PADDING * 2, 0xFFFFFFFF);
        textRenderer.draw(matrices, "∨", this.x + this.width - 8, this.y + ENTRY_PADDING * 2 + 1, 0xFFFFFFFF); //∧∨
    }

    private void drawEntries(MatrixStack matrices, TextRenderer textRenderer, int mouseX, int mouseY, ClickGUI gui) {
        int listHeight = 0;
        CategoryModuleEntry hoveredEntry = null;
        for (CategoryModuleEntry entry : this.entries) {
            int x = this.x;
            int y = this.y + HEADER_HEIGHT + listHeight;
            int width = this.width;
            int height = ENTRY_HEIGHT + ENTRY_PADDING * 2;

            listHeight += height;

            DrawableHelper.fill(matrices, x, y, x + width, y + height, entry.isEnabled() ? 0xCC3CFF00 : 0xCC000000);
            DrawableHelper.fill(matrices, x, y + height, x + width, y + height - 1, this.color);
            textRenderer.draw(matrices, entry.getName(), x + this.width / 2 - textRenderer.getWidth(entry.getName()) / 2, y + ENTRY_PADDING * 2, 0xFFFFFFFF);

            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                hoveredEntry = entry;

                //Toggle Module
                if (GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
                    if (!entry.wasToggled()) {
                        entry.toggle();
                        entry.setWasToggled(true);
                    }
                } else {
                    entry.setWasToggled(false);
                }

            }

        }
        if(hoveredEntry != null) {
            List<OrderedText> lines = textRenderer.wrapLines(StringVisitable.plain("§7" + hoveredEntry.getDescription() + "\n§bBinding: " + hoveredEntry.getBinding()), 200);
            gui.renderOrderedTooltip(matrices, lines, mouseX, mouseY);
        }
    }

    protected void addEntry(CategoryModuleEntry entry) {
        this.entries.add(entry);
    }

    protected void removeEntry(CategoryModuleEntry entry) {
        this.entries.remove(entry);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}

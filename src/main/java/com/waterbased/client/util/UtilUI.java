package com.waterbased.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public class UtilUI {
    public static final int MAX_HELD_ITEM_FADE_OUT = 40;

    public static int OWN_HELD_ITEM_FADE_OUT = MAX_HELD_ITEM_FADE_OUT;
    public static @Nullable MutableText displayItemNames = null;

    public static void displayItemNameText(String text) {
        displayItemNameText(text, TextColor.fromFormatting(Formatting.WHITE));
    }

    public static void displayItemNameText(String text, TextColor color) {
        displayItemNameText(text, color, false, false, false, false, false);
    }

    public static void displayItemNameText(String text, TextColor color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
        displayItemNames = MutableText.of(new LiteralTextContent(text))
                .setStyle(Style.EMPTY.withColor(color)
                        .withBold(bold)
                        .withItalic(italic)
                        .withUnderline(underlined)
                        .withStrikethrough(strikethrough)
                        .withObfuscated(obfuscated));
        MatrixStack matrixStack = new MatrixStack();
        OWN_HELD_ITEM_FADE_OUT = MAX_HELD_ITEM_FADE_OUT;
        MinecraftClient.getInstance().inGameHud.renderHeldItemTooltip(matrixStack);
    }
}

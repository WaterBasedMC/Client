package com.waterbased.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatManager {

    private static final String PREFIX = "§8[§9WaterBased§8]§r ";

    public void send(String message) {
        send(message, PREFIX);
    }

    public void send(String message, String prefix) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(prefix + message));
    }

}

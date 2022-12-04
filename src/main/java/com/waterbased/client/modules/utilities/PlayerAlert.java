package com.waterbased.client.modules.utilities;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class PlayerAlert extends Module {

    public static final Text MESSAGE_TEMPLATE = Text.of("§bPlayer %s is nearby. (%s blocks away)");

    public PlayerAlert() {
        super("PlayerAlert", "Alerts you when a player is nearby");
    }

    @Override
    public void onEnable() {
        World world = MinecraftClient.getInstance().world;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (world == null || player == null) return;

        // Gets a list of all players in the world filters out the current player
        List<? extends PlayerEntity> otherPlayers = world.getPlayers();
        otherPlayers = otherPlayers.stream().filter(p -> p instanceof OtherClientPlayerEntity).toList();

        if (otherPlayers.size() > 0) {
            otherPlayers.forEach(otherPlayer -> {
                assert MinecraftClient.getInstance().player != null;
                float distanceToPlayer = MinecraftClient.getInstance().player.distanceTo(otherPlayer);
                distanceToPlayer = Math.round(distanceToPlayer * 100.0f) / 100.0f;
                MinecraftClient.getInstance().inGameHud.getChatHud()
                        .addMessage(Text.of(String.format(MESSAGE_TEMPLATE.getString(), otherPlayer.getDisplayName()
                                .getString(), distanceToPlayer)));
            });
        } else {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(Text.of("§bNo players nearby."));
        }
    }

    @Override
    public void onDisable() {
    }

}

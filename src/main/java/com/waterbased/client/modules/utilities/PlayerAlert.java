package com.waterbased.client.modules.utilities;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import com.waterbased.client.util.UtilUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerAlert extends Module {

    public static final String MESSAGE_TEMPLATE = "§b%s is %.2f blocks away";
    public static final String MESSAGE_TEMPLATE_TOOLTIP = "%s%s: (%.2f blocks) | Total: %d";
    public final List<Text> messages = new ArrayList<>();
    public final List<OtherClientPlayerEntity> nearbyPlayers = new ArrayList<>();
    public static final List<Float> NEARBY_PLAYER_ALERT_DISTANCES = new ArrayList<>(List.of(10f, 50f));

    private float oldNearestDistance = Float.MAX_VALUE;
    private int tickCounter = 0;

    public PlayerAlert() {
        super("PlayerAlert", "Alerts you when a player is nearby");
    }

    @Override
    public void onEnable() {
        tickCounter = 0;
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
                messages.add(Text.of(String.format(MESSAGE_TEMPLATE, otherPlayer.getName()
                        .getString(), distanceToPlayer)));
                nearbyPlayers.add((OtherClientPlayerEntity) otherPlayer);
            });
        } else {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(Text.of("§bNo players nearby."));
        }
    }

    public void onNewPlayer(OtherClientPlayerEntity player) {
        if (!nearbyPlayers.contains(player)) {
            nearbyPlayers.add(player);
        }
    }

    @Override
    public void onTick() {
        if (tickCounter++ == 5) {
            tickCounter = 0;
        }
        if (!this.isEnabled()) return;
        if (this.messages.size() > 0) {
            messages.forEach(message -> Client.INSTANCE.chatManager.send(
                    message.getString(),
                    "[§cPlayerAlert§r] "
            ));
            messages.clear();
        }
        if (tickCounter == 0) {
            printNearestPlayer();
        }
    }

    private void printNearestPlayer() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            Client.LOGGER.warning("Player is null");
            return;
        }
        cleanupNearbyPlayer();
        OtherClientPlayerEntity nearestPlayer = getNearbyPlayer();
        if (nearestPlayer != null) {
            float distanceToPlayer = player.distanceTo(nearestPlayer);
            nearbyAlertToChat(distanceToPlayer, nearestPlayer);
            oldNearestDistance = distanceToPlayer;
            TextColor color = TextColor.fromFormatting(distanceToPlayer < 10 ? Formatting.RED :
                    distanceToPlayer < 25 ? Formatting.YELLOW : Formatting.GREEN);
            assert color != null;
            UtilUI.displayItemNameText(String.format(MESSAGE_TEMPLATE_TOOLTIP, "",
                    nearestPlayer.getName().getString(), distanceToPlayer, nearbyPlayers.size()
            ), color);
        }
    }

    private void nearbyAlertToChat(float distanceToPlayer, OtherClientPlayerEntity nearestPlayer) {
        NEARBY_PLAYER_ALERT_DISTANCES.sort(Float::compareTo);
        for (float distance : NEARBY_PLAYER_ALERT_DISTANCES) {
            if (oldNearestDistance > distance && distanceToPlayer <= distance) {
                Client.INSTANCE.chatManager.send(
                        String.format(MESSAGE_TEMPLATE, nearestPlayer.getName()
                                .getString(), distanceToPlayer),
                        "[§cPlayerAlert§r] "
                );
                break;
            }
        }
    }

    private void cleanupNearbyPlayer() {
        nearbyPlayers.removeIf(OtherClientPlayerEntity::isRemoved);
    }

    private @Nullable OtherClientPlayerEntity getNearbyPlayer() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;
        OtherClientPlayerEntity nearestPlayer = null;
        float oldDistance = Integer.MAX_VALUE;
        for (OtherClientPlayerEntity curPlayer : nearbyPlayers) {
            if (nearestPlayer == null) {
                nearestPlayer = curPlayer;
                oldDistance = player.distanceTo(curPlayer);
            } else {
                if (player.distanceTo(curPlayer) < oldDistance) {
                    nearestPlayer = curPlayer;
                }
            }
            if (oldDistance < 2) {
                break;
            }
        }
        return nearestPlayer;
    }

    @Override
    public void onDisable() {
    }

}

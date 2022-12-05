package com.waterbased.client.modules.movement;

import com.mojang.authlib.GameProfile;
import com.waterbased.client.Client;
import com.waterbased.client.mixin.PlayerListEntryInvoker;
import com.waterbased.client.modules.Module;
import com.waterbased.client.util.UtilEntity;
import com.waterbased.client.util.UtilUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.GameMode;

import java.util.Objects;
import java.util.UUID;

public class SpectatorCam extends Module {
    private OtherClientPlayerEntity clone = null;
    private GameMode oldGameMode = null;

    public SpectatorCam() {
        super("SpectatorCam", "Allows you to fly around in spectator mode", InputUtil.GLFW_KEY_M);
    }


    public OtherClientPlayerEntity getClone() {
        return clone;
    }

    @Override
    public void onEnable() {
        if (MinecraftClient.getInstance().world == null) {
            Client.LOGGER.severe("World is null");
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            Client.LOGGER.severe("Player is null");
            return;
        }
        this.clone = new OtherClientPlayerEntity(MinecraftClient.getInstance().world, new GameProfile(UUID.randomUUID(), player.getName()
                .getString()), player.getPublicKey());
        this.clone.setId(Integer.MAX_VALUE);
        this.clone.copyPositionAndRotation(player);
        this.clone.setHeadYaw(player.getHeadYaw());

        MinecraftClient.getInstance().world.addEntity(this.clone.getId(), this.clone);
        // change game mode to spectator
        PlayerListEntry playerListEntry = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler())
                .getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            Client.LOGGER.severe("PlayerListEntry is null");
            return;
        }
        if (MinecraftClient.getInstance().interactionManager == null) {
            Client.LOGGER.severe("InteractionManager is null");
            return;
        }
        MinecraftClient.getInstance().interactionManager.setGameMode(GameMode.SPECTATOR);
        this.oldGameMode = playerListEntry.getGameMode();
        ((PlayerListEntryInvoker) playerListEntry).setGameModeInvoker(GameMode.SPECTATOR);
        UtilEntity.setEntityGlow(this.clone, true);
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            Client.LOGGER.severe("Player is null");
            return;
        }
        if (this.clone == null) {
            Client.LOGGER.severe("Clone is null");
            return;
        }
        if (MinecraftClient.getInstance().world == null) {
            Client.LOGGER.severe("World is null");
            return;
        }
        player.copyPositionAndRotation(this.clone);
        player.setHeadYaw(this.clone.getHeadYaw());
        player.setVelocity(0, 0, 0);
        UtilEntity.setEntityGlow(this.clone, false);

        MinecraftClient.getInstance().world.removeEntity(this.clone.getId(), Entity.RemovalReason.DISCARDED);
        // change game mode back to survival
        PlayerListEntry playerListEntry = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler())
                .getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            Client.LOGGER.severe("PlayerListEntry is null");
            return;
        }
        if (this.oldGameMode == null) return;
        ((PlayerListEntryInvoker) playerListEntry).setGameModeInvoker(this.oldGameMode);
        if (MinecraftClient.getInstance().interactionManager == null) {
            Client.LOGGER.severe("InteractionManager is null");
            return;
        }
        MinecraftClient.getInstance().interactionManager.setGameMode(this.oldGameMode);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true)); // Prevents autokick for flying

        float distanceToClone = player.distanceTo(this.clone);
        distanceToClone = (float) (Math.round(distanceToClone * 100.0) / 100.0);
        UtilUI.displayItemNameText("Distance: " + String.format("%.2f", distanceToClone));
    }

}

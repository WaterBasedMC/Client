package com.waterbased.client.modules;

import com.mojang.authlib.GameProfile;
import com.waterbased.client.Client;
import com.waterbased.client.mixin.PlayerListEntryInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.GameMode;

import java.util.Objects;
import java.util.UUID;

public class FreeCam extends Module {
    public boolean flying = false;
    private PlayerEntity clone = null;
    private GameMode oldGameMode = null;

    public FreeCam() {
        super("FreeCam", "Allows you to fly around and interact with the world without moving your player", InputUtil.GLFW_KEY_I);
    }

    @Override
    public void onEnable() {
        assert MinecraftClient.getInstance().world != null;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        this.clone = new OtherClientPlayerEntity(MinecraftClient.getInstance().world, new GameProfile(UUID.randomUUID(), player.getName().getString()), player.getPublicKey());
        this.clone.copyPositionAndRotation(player);
        this.clone.setHeadYaw(player.getHeadYaw());
        this.clone.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 9999)); // TODO: Not working

        MinecraftClient.getInstance().world.addEntity(this.clone.getId(), this.clone);
        // change game mode to spectator
        PlayerListEntry playerListEntry = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerListEntry(player.getUuid());
        if (playerListEntry != null) {
            this.oldGameMode = playerListEntry.getGameMode();
            ((PlayerListEntryInvoker) playerListEntry).setGameModeInvoker(GameMode.SPECTATOR); // TODO: Change to Creative Inventory? To break and place any block
            player.getAbilities().flying = true;
        }

        //TODO: Ignore resync packets during freecam
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        assert MinecraftClient.getInstance().world != null;
        player.copyPositionAndRotation(this.clone);
        player.setHeadYaw(this.clone.getHeadYaw());
        player.setVelocity(0, 0, 0);

        MinecraftClient.getInstance().world.removeEntity(this.clone.getId(), Entity.RemovalReason.DISCARDED);
        // change game mode back to survival
        PlayerListEntry playerListEntry = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerListEntry(player.getUuid());
        if (playerListEntry != null && this.oldGameMode != null) {
            ((PlayerListEntryInvoker) playerListEntry).setGameModeInvoker(this.oldGameMode);
            player.getAbilities().flying = false;
        }
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    }

    @Override
    public void onKey() {
        Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
    }
}

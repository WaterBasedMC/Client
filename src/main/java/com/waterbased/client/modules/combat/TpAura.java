package com.waterbased.client.modules.combat;

import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class TpAura extends Module {

    OtherClientPlayerEntity lockedTarget;
    int lastHitTicks = 0;
    int HIT_DELAY = 5;

    public TpAura() {
        super("TpAura", "Teleports you around the enemy", InputUtil.GLFW_KEY_X);
    }

    public static void teleportBehindEntity(OtherClientPlayerEntity target) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (player == null || networkHandler == null) return;

        // teleport behind enemy, based on their yaw
        double yaw = target.getYaw();
        double x = target.getX() + Math.sin(Math.toRadians(yaw));
        double z = target.getZ() - Math.cos(Math.toRadians(yaw));
        Vec3d behindTargetPos = new Vec3d(x, player.getY(), z);
        Vec3d vecToPos = behindTargetPos.subtract(player.getPos());
        behindTargetPos = player.getPos().add(vecToPos.multiply(1.7));
        networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(behindTargetPos.getX(), behindTargetPos.getY(), behindTargetPos.getZ(), player.isOnGround()));
        player.updatePosition(behindTargetPos.getX(), behindTargetPos.getY(), behindTargetPos.getZ());
    }

    public static void lookAtEntity(OtherClientPlayerEntity target) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        float old_pitch = player.getPitch();
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, target.getPos());
        player.setPitch(old_pitch);
    }

    @Override
    public void onTick() {
        if (this.lockedTarget == null) return;
        lastHitTicks++;
        if (this.lastHitTicks < HIT_DELAY) return;
        this.lastHitTicks = 0;

        if (this.lockedTarget.isRemoved() || isInvulnerable(this.lockedTarget)) { // if target is not rendered or left or can't be attacked
            this.lockedTarget = null;
            return;
        }

        ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (interactionManager == null || player == null) return;

        interactionManager.attackEntity(player, lockedTarget);
    }

    public void onAttack(OtherClientPlayerEntity target) {
        if (isInvulnerable(target)) return;
        this.lockedTarget = target;
        this.lastHitTicks = 0;
        teleportBehindEntity(target);
        lookAtEntity(target);
    }

    public static boolean isInvulnerable(OtherClientPlayerEntity target) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (target == null || networkHandler == null || player == null) return true;
        PlayerListEntry targetEntry = MinecraftClient.getInstance()
                .getNetworkHandler()
                .getPlayerListEntry(target.getUuid());
        return (targetEntry == null || targetEntry.getGameMode() == GameMode.CREATIVE || target.isDead()) && player.isSpectator();
    }

    @Override
    public void onEnable() {
        // see mixin\PlayerEntityMixin
    }

    @Override
    public void onDisable() {
        this.lockedTarget = null;
        this.lastHitTicks = 0;
    }

}


package com.waterbased.client.modules.combat;
import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class TpAura extends Module {

    PlayerEntity enemy;
    int lastHitTicks = 0;
    int HIT_DELAY = 5;
    public TpAura() {
        super("TpAura", "Teleports you around the enemy", InputUtil.GLFW_KEY_X);
    }

    public void teleportBehindEntity(Entity target) {
        if (target instanceof PlayerEntity) {
            enemy = (PlayerEntity) target;
            PlayerEntity player = MinecraftClient.getInstance().player;
            //teleport behind enemy, based on their yaw
            double yaw = enemy.getYaw();
            double x = enemy.getX() + Math.sin(Math.toRadians(yaw));
            double z = enemy.getZ() - Math.cos(Math.toRadians(yaw));


            Vec3d behindEnemy = new Vec3d(x,player.getY(),z);
            ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
            if (networkHandler == null) return;
            networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(behindEnemy.getX(), behindEnemy.getY(), behindEnemy.getZ(), player.isOnGround()));
            player.updatePosition(behindEnemy.getX(), behindEnemy.getY(), behindEnemy.getZ());
        }
    }

    public void lookAtEntity(Entity enemy) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        float old_pitch = player.getPitch();
        player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, enemy.getPos());
        player.setPitch(old_pitch);
    }

    @Override
    public void onTick() {
        if (enemy != null && lastHitTicks < HIT_DELAY) {
            ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
            if (interactionManager == null) return;
            if(enemy.isDead()) {
                enemy = null;
                lastHitTicks = 0;
            }
            interactionManager.attackEntity(MinecraftClient.getInstance().player, enemy);
            lastHitTicks = 0;
        }
        lastHitTicks += 1;
    }

    @Override
    public void onEnable() {
        // see mixin\PlayerEntityMixin
    }

    @Override
    public void onDisable() {
        enemy = null;
    }

}


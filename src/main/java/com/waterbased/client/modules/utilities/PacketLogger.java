package com.waterbased.client.modules.utilities;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;

import java.util.ArrayList;
import java.util.Locale;

public class PacketLogger extends Module {
    private static ArrayList<Class> watchedPackets = new ArrayList<>();
    private static boolean enableRecieved;
    private static boolean enableSent;

    public PacketLogger() {
        super("Packet Logger", "Logs all packets sent and received in the Chat", InputUtil.GLFW_KEY_K);

        enableSent = true;
        enableRecieved = false;
        watchedPackets.add(BlockUpdateS2CPacket.class);
        watchedPackets.add(EntityPositionS2CPacket.class);
        watchedPackets.add(PlayerMoveC2SPacket.class);


    }

    private static String getSideName(NetworkSide side) {
        if (side == NetworkSide.CLIENTBOUND) return "Client";
        if (side == NetworkSide.SERVERBOUND) return "Server";

        return side.name().toLowerCase(Locale.ROOT);
    }

    public static void logSentPacket(Packet<?> packet, NetworkSide side) {
        if (!enableSent || packet == null) return;

        String sideName = PacketLogger.getSideName(side);
        if (watchedPackets.stream().filter(x-> x.isInstance(packet)).findAny().orElse(null) != null) {
            Client.LOGGER.info("§6[" + sideName + "]§6 Sending packet with name '§7" + packet.getClass().getName());

            if (packet instanceof PlayerMoveC2SPacket) {
                PlayerMoveC2SPacket playerMoveC2SPacket = (PlayerMoveC2SPacket) packet;
                Client.LOGGER.info("Packet:\n§r" +
                        "Pos X: §6" + playerMoveC2SPacket.getX(0) + "§r\n" +
                        "Pos Y: §6" + playerMoveC2SPacket.getY(0) + "§r\n" +
                        "Pos Z: §6" + playerMoveC2SPacket.getZ(0) + "§r\n" +
                        "Yaw: §6" + playerMoveC2SPacket.getYaw(0) + "§r\n" +
                        "Pitch: §6" + playerMoveC2SPacket.getPitch(0) + "§r\n" +
                        "OnGround: §6" + playerMoveC2SPacket.isOnGround());
            }
        }else{
            Client.LOGGER.info("§6[" + sideName + "]§6 Sending packet with name '§7" + packet.getClass().getName());
        }

    }

    public static void logReceivedPacket(Packet<?> packet, NetworkSide side) {
        if (!enableRecieved || packet == null) return;

        String sideName = PacketLogger.getSideName(side);

        if (watchedPackets.stream().filter(x-> x.isInstance(packet)).findAny().orElse(null) != null) {
            Client.LOGGER.info("§6[" + sideName + "]§r Received packet with name '§7" + packet.getClass().getName());
            if (packet instanceof EntityPositionS2CPacket) {
                EntityPositionS2CPacket entityPositionS2CPacket = (EntityPositionS2CPacket) packet;
                PlayerEntity player = MinecraftClient.getInstance().player;

                if(player != null && entityPositionS2CPacket.getId() != player.getId())return;

                Client.LOGGER.info("Packet:\n§r" +
                        "Entity ID: §6" + entityPositionS2CPacket.getId() + "§r\n" +
                        "Pos X: §6" + entityPositionS2CPacket.getX() + "§r\n" +
                        "Pos Y: §6" + entityPositionS2CPacket.getY() + "§r\n" +
                        "Pos Z: §6" + entityPositionS2CPacket.getZ() + "§r\n" +
                        "OnGround: §6" + entityPositionS2CPacket.isOnGround());
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}

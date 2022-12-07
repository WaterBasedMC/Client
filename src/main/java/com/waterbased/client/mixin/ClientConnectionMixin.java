package com.waterbased.client.mixin;

import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.utilities.PacketLogger;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    @Final
    private NetworkSide side;
    private static PacketLogger packetLogger = ModuleManager.INSTANCE.getModule(PacketLogger.class);
    @Inject(method = "sendImmediately", at = @At("HEAD"))
    private void logSentPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if(packetLogger.isEnabled()) {
            packetLogger.logSentPacket(packet, side);
        }
    }

    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static void logReceivedPacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if(packetLogger.isEnabled()) {
            packetLogger.logReceivedPacket(packet, listener.getConnection().getSide());
        }
    }
}

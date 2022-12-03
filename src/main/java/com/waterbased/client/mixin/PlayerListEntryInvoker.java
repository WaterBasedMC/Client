package com.waterbased.client.mixin;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerListEntry.class)
public interface PlayerListEntryInvoker {

    @Invoker("setGameMode")
    void setGameModeInvoker(GameMode gameMode);
}

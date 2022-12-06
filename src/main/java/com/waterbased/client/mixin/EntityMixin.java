package com.waterbased.client.mixin;

import com.waterbased.client.Client;
import com.waterbased.client.modules.movement.Jesus;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {


    @Inject(at = @At("TAIL"), method = "move")
    private void move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof ClientPlayerEntity)) return;

        // Module: Jesus
        Jesus jesus = Client.INSTANCE.MODULE_MANAGER.getModule(Jesus.class);
        if (jesus != null && jesus.isEnabled()) {
            jesus.onMove(entity);
        }
    }

}

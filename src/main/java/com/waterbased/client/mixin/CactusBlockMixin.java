package com.waterbased.client.mixin;

import com.waterbased.client.modules.ModuleManager;
import com.waterbased.client.modules.utilities.AntiCactus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public class CactusBlockMixin extends Block {

    public CactusBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "getCollisionShape", cancellable = true)
    public void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (ModuleManager.INSTANCE.getModule(AntiCactus.class) != null &&
                ModuleManager.INSTANCE.getModule(AntiCactus.class).isEnabled()) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}

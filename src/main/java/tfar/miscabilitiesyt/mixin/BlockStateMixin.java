package tfar.miscabilitiesyt.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.miscabilitiesyt.MiscAbilitiesYT;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateMixin {

	@Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",at = @At("HEAD"),cancellable = true)
	private void waterWalk(BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
		if (MiscAbilitiesYT.waterWalk((BlockBehaviour.BlockStateBase)(Object)this,pLevel, pPos,pContext)) {
			cir.setReturnValue(Shapes.block());
		}
	}
}

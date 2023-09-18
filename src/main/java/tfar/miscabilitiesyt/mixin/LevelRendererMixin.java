package tfar.miscabilitiesyt.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.miscabilitiesyt.ClientModAbilties;

import java.util.Iterator;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/OutlineBufferSource;setColor(IIII)V",shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILHARD)
    private void changeColors(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline,
                              Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix,
                              CallbackInfo ci, ProfilerFiller profilerfiller, boolean flag, Vec3 vec3, double d0, double d1,
                              double d2, Matrix4f matrix4f, boolean flag1, Frustum frustum, float f, boolean flag2, boolean flag3,
                              MultiBufferSource.BufferSource multibuffersource$buffersource, Iterator var26, Entity entity,
                              BlockPos blockpos, MultiBufferSource multibuffersource, OutlineBufferSource outlinebuffersource,
                              int i, int j, int k, int l, int i1) {

        if (ClientModAbilties.clientModAbilities.vision) {
            outlinebuffersource.setColor(255,0,0,255);
        }
    }
}

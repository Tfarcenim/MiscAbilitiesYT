package tfar.miscabilitiesyt.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.miscabilitiesyt.ClientModAbilties;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "shouldEntityAppearGlowing",at = @At("HEAD"),cancellable = true)
    private void allGlow(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (ClientModAbilties.clientModAbilities.vision) {
            cir.setReturnValue(true);
        }
    }
}

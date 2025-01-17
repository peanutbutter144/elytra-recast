package me.lunaluna.fabric.elytrarecast.mixin;

import me.lunaluna.fabric.elytrarecast.Startup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
public class PlayerJumpCooldownMixin {

    @Shadow private int jumpingCooldown;
    private ClientPlayerEntity player() {
        return MinecraftClient.getInstance().player;
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void reduceCooldown(CallbackInfo ci) {
        if (!MinecraftClient.getInstance().isInSingleplayer()) return;
        if (Startup.INSTANCE.getConfig().getJumpEnabled() && (jumpingCooldown > Startup.INSTANCE.getConfig().getJumpCooldown()) && equals(player())) {
            jumpingCooldown = Startup.INSTANCE.getConfig().getJumpCooldown();
        }
    }
}
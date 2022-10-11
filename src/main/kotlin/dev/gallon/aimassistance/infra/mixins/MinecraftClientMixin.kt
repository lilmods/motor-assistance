package dev.gallon.aimassistance.infra.mixins

import dev.gallon.aimassistance.infra.events.RenderEvent
import dev.gallon.aimassistance.infra.events.SingleEventBus
import dev.gallon.aimassistance.infra.events.TickEvent
import net.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(MinecraftClient::class)
class MinecraftClientMixin {
    @Inject(method = ["render(Z)V"], at = [At("HEAD")])
    private fun renderEvent(tick: Boolean, info: CallbackInfo) {
        SingleEventBus.send(RenderEvent())
    }

    @Inject(method = ["tick()V"], at = [At("TAIL")])
    private fun tickEvent(info: CallbackInfo) {
        SingleEventBus.send(TickEvent())
    }
}

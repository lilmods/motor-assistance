package dev.gallon.aimassistance.fabric.mixins

import dev.gallon.aimassistance.fabric.events.MouseMovedEvent
import dev.gallon.aimassistance.fabric.events.SingleEventBus
import net.minecraft.client.Mouse
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(Mouse::class)
class MouseMixin {
    @Inject(method = ["onCursorPos(JDD)V"], at = [At("TAIL")])
    private fun mouseMoved(window: Long, x: Double, y: Double, info: CallbackInfo) {
        SingleEventBus.send(MouseMovedEvent(x = x, y = y))
    }
}

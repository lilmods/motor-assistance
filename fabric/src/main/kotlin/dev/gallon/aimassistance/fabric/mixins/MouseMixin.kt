package dev.gallon.aimassistance.fabric.mixins

import dev.gallon.aimassistance.fabric.events.LeftMouseClickEvent
import dev.gallon.aimassistance.fabric.events.MouseMoveEvent
import dev.gallon.aimassistance.fabric.events.SingleEventBus
import net.minecraft.client.Mouse
import org.lwjgl.glfw.GLFW
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(Mouse::class)
class MouseMixin {
    @Inject(method = ["onCursorPos(JDD)V"], at = [At("TAIL")])
    private fun mouseMoved(window: Long, x: Double, y: Double, info: CallbackInfo) {
        SingleEventBus.send(MouseMoveEvent(x = x, y = y))
    }

    @Inject(method = ["onMouseButton(JIII)V"], at = [At("TAIL")])
    private fun mouseClicked(window: Long, button: Int, action: Int, mods: Int, info: CallbackInfo) {
        when (button) {
            GLFW.GLFW_MOUSE_BUTTON_LEFT -> when(action) {
                GLFW.GLFW_PRESS -> SingleEventBus.send(LeftMouseClickEvent)
            }
        }
    }
}

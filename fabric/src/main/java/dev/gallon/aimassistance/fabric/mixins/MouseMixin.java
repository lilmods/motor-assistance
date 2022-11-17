package dev.gallon.aimassistance.fabric.mixins;

import dev.gallon.aimassistance.fabric.events.LeftMouseClickEvent;
import dev.gallon.aimassistance.fabric.events.MouseMoveEvent;
import dev.gallon.aimassistance.fabric.events.SingleEventBus;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onCursorPos(JDD)V", at = @At("TAIL"))
    private void mouseMoved(long window, double x, double y, CallbackInfo ci) {
        SingleEventBus.INSTANCE.send(new MouseMoveEvent(x, y));
    }

    @Inject(method = "onMouseButton(JIII)V", at = @At("TAIL"))
    private void mouseClicked(long window, int button, int action, int mods, CallbackInfo info) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            SingleEventBus.INSTANCE.send(new LeftMouseClickEvent());
        }
    }
}

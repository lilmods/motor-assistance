package dev.gallon.motorassistance.forge.adapters

import dev.gallon.motorassistance.common.interfaces.Mouse
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

object ForgeMouseAdapter : Mouse {
    private var moved = false
    private var leftClicked = false

    @JvmStatic
    @SubscribeEvent
    fun onMouseDraggedEvent(mouseEvent: ScreenEvent.MouseDragged) {
        moved = true
    }

    @JvmStatic
    @SubscribeEvent
    fun onMouseDraggedEvent(mouseEvent: ScreenEvent.MouseButtonReleased) {
        if (mouseEvent.button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            leftClicked = true
        }
    }

    override fun wasLeftClicked(): Boolean = leftClicked.also { leftClicked = false }

    override fun wasMoved(): Boolean = moved.also { moved = false }
}

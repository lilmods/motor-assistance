package dev.gallon.motorassistance.forge.adapters

import dev.gallon.motorassistance.common.interfaces.Mouse
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

class ForgeMouseAdapter : Mouse {
    private var moved = false
    private var leftClicked = false
    private var skipEventCount = 2
    private var prevX = -1.0
    private var prevY = -1.0

    @SubscribeEvent
    fun onClientTick(clientTickEvent: TickEvent.ClientTickEvent) {
        checkForMouseMove()
    }

    @SubscribeEvent
    fun onMouseButtonReleased(mouseEvent: InputEvent.MouseButton.Post) {
        if (mouseEvent.button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            leftClicked = true
        }
    }

    private fun checkForMouseMove() {
        val currX: Double = Minecraft.getInstance().mouseHandler.xpos()
        val currY: Double = Minecraft.getInstance().mouseHandler.ypos()

        if (prevX != -1.0 && prevY != -1.0 && !moved) {
            moved = prevX != currX || prevY != currY
        }

        if (skipEventCount == 0) {
            prevX = currX
            prevY = currY
            skipEventCount = 2
        } else {
            skipEventCount--
        }
    }

    override fun wasLeftClicked(): Boolean = leftClicked.also { leftClicked = false }

    override fun wasMoved(): Boolean = moved.also { moved = false }
}

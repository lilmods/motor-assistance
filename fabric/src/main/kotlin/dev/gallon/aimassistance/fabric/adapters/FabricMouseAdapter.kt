package dev.gallon.aimassistance.fabric.adapters

import dev.gallon.aimassistance.core.interfaces.Mouse
import dev.gallon.aimassistance.fabric.events.MouseMovedEvent
import dev.gallon.aimassistance.fabric.events.SingleEventBus
import net.minecraft.client.Mouse as FabricMouse

class FabricMouseAdapter(
    private val mouse: FabricMouse
) : Mouse {

    private var mouseMoved = false

    init {
        SingleEventBus.register<MouseMovedEvent> { mouseMoved = true }
    }

    override fun wasLeftClicked(): Boolean = mouse.wasLeftButtonClicked()

    override fun wasMoved(): Boolean = mouseMoved.also { mouseMoved = false }
}

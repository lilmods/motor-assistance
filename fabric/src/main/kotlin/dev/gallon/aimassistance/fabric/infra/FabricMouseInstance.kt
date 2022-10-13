package dev.gallon.aimassistance.fabric.infra

import dev.gallon.aimassistance.fabric.domain.MouseInstance
import dev.gallon.aimassistance.fabric.infra.events.MouseMovedEvent
import dev.gallon.aimassistance.fabric.infra.events.SingleEventBus
import net.minecraft.client.Mouse

class FabricMouseInstance(
    private val mouse: Mouse
) : MouseInstance {

    var mouseMoved = false

    init {
        SingleEventBus.register<MouseMovedEvent> { mouseMoved = true }
    }

    override fun wasLeftClicked(): Boolean = mouse.wasLeftButtonClicked()

    override fun wasMoved(): Boolean = mouseMoved.also { mouseMoved = false }
}

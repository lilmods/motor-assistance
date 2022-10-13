package dev.gallon.aimassistance.fabric.adapters

import dev.gallon.aimassistance.core.interfaces.Mouse
import dev.gallon.aimassistance.fabric.events.LeftMouseClickEvent
import dev.gallon.aimassistance.fabric.events.MouseMoveEvent
import dev.gallon.aimassistance.fabric.events.SingleEventBus

class FabricMouseAdapter: Mouse {

    private var moved = false
    private var leftClicked = false

    init {
        SingleEventBus.register<MouseMoveEvent> { moved = true }
        SingleEventBus.register<LeftMouseClickEvent> { leftClicked = true }
    }

    override fun wasLeftClicked(): Boolean = leftClicked.also { leftClicked = false }

    override fun wasMoved(): Boolean = moved.also { moved = false }
}

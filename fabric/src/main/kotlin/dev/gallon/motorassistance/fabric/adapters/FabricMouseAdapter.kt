package dev.gallon.motorassistance.fabric.adapters

import dev.gallon.motorassistance.core.interfaces.Mouse
import dev.gallon.motorassistance.fabric.events.LeftMouseClickEvent
import dev.gallon.motorassistance.fabric.events.MouseMoveEvent
import dev.gallon.motorassistance.fabric.events.SingleEventBus

class FabricMouseAdapter : Mouse {

    private var moved = false
    private var leftClicked = false

    init {
        SingleEventBus.register<MouseMoveEvent> { moved = true }
        SingleEventBus.register<LeftMouseClickEvent> { leftClicked = true }
    }

    override fun wasLeftClicked(): Boolean = leftClicked.also { leftClicked = false }

    override fun wasMoved(): Boolean = moved.also { moved = false }
}

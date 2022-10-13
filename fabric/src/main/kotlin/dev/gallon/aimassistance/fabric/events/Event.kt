package dev.gallon.aimassistance.fabric.events

sealed interface Event

object RenderEvent : Event

object TickEvent : Event

class MouseMoveEvent(
    val x: Double,
    val y: Double
): Event

object LeftMouseClickEvent : Event

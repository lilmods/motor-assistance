package dev.gallon.motorassistance.fabric.events

sealed interface Event

object RenderEvent : Event

object TickEvent : Event

data class MouseMoveEvent(
    val x: Double,
    val y: Double
) : Event

object LeftMouseClickEvent : Event

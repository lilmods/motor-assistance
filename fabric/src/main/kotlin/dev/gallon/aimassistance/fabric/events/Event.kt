package dev.gallon.aimassistance.fabric.events

sealed interface Event

class RenderEvent : Event

class TickEvent : Event

class MouseMovedEvent(
    val x: Double,
    val y: Double
): Event

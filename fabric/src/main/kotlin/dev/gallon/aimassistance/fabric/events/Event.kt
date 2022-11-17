package dev.gallon.aimassistance.fabric.events

sealed interface Event

class RenderEvent : Event

class TickEvent : Event

class MouseMoveEvent(
    val x: Double,
    val y: Double
): Event

class LeftMouseClickEvent : Event

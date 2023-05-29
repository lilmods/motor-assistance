package dev.gallon.aimassistance.fabric.events

sealed interface Event

data class TickEvent(
    val something: String
) : Event

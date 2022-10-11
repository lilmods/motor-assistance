package dev.gallon.aimassistance.infra.events

sealed interface Event

class RenderEvent : Event

class TickEvent : Event

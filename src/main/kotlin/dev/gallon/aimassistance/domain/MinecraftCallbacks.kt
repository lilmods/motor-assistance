package dev.gallon.aimassistance.domain

interface MinecraftCallbacks {
    fun onMouseClick(callback: () -> Unit)
    fun onMouseMove(callback: () -> Unit)
}

package dev.gallon.aimassistance.domain

data class Rotation(
    val pitch: Double,
    val yaw: Double
) {
    override fun toString(): String = "pitch=$pitch yaw=$yaw"
}

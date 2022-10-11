package dev.gallon.aimassistance.domain

data class Position(
    val x: Double,
    val y: Double,
    val z: Double
) {
    operator fun minus(other: Position): Position =
        Position(
            x = x - other.x,
            y = y - other.y,
            z = z - other.z
        )
}

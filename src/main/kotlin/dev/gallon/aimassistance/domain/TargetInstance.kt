package dev.gallon.aimassistance.domain

sealed interface TargetInstance {
    fun getPosition(): Position
}

interface EntityInstance : TargetInstance {
    fun getEyesHeight(): Double
    fun getEyesPosition(): Position = getPosition().run { copy(y = y + getEyesHeight()) }
}

interface PlayerInstance : EntityInstance {
    fun setRotations(rotations: Vec2d)
}


interface BlockInstance : TargetInstance {
}

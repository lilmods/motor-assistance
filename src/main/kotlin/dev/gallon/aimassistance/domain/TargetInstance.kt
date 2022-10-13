package dev.gallon.aimassistance.domain

sealed interface TargetInstance {
    fun getPosition(): Position
}

interface EntityInstance : TargetInstance {
    fun getEyesHeight(): Double
    fun getEyesPosition(): Position = getPosition().run { copy(y = y + getEyesHeight()) }
    fun getRotations(): Rotation
}

interface PlayerInstance : EntityInstance {
    fun setRotation(rotations: Rotation)

    /**
     * @return true if the player is in game and not in any GUIs
     */
    fun canInteract(): Boolean

    fun findMobsAroundPlayer(range: Double): List<EntityInstance>

    fun rayTrace(reach: Double, source: Position, direction: Rotation): BlockInstance?
}


interface BlockInstance : TargetInstance {
    fun getFacePosition(): Position
}

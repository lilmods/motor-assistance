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

    /**
     * @return true if the player is in game and not in any GUIs
     */
    fun canInteract(): Boolean

    fun findMobsAroundPlayer(range: Double): List<EntityInstance>

    /**
     * @param entityRepositories the entities
     * @return the closest entity from where the player is aiming. Null if the entities list is empty.
     */
    fun getClosestEntityToPlayerAim(entites: List<EntityInstance>): EntityInstance?


}


interface BlockInstance : TargetInstance {
}

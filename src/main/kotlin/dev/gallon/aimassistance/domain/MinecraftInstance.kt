package dev.gallon.aimassistance.domain

interface MinecraftInstance {
    /**
     * @return true if the player is in game and not in any GUIs
     */
    fun isPlayerInGame(): Boolean

    /**
     * @return true if the attack key is being pressed
     */
    fun attackKeyPressed(): Boolean

    /**
     * @return true if the player is aiming an entity that is a mob (not a player or an item)
     */
    fun playerAimingMob(): Boolean

    /**
     * TODO
     */
    fun getEntitiesAroundPlayer(range: Double): List<EntityInstance>

    /**
     * @param maxRange the maximum range between the player eyes and the block (unit: 1.0 = 1 block)
     * @return the block the user is looking at if there's one
     */
    fun getPointedBlock(maxRange: Double): BlockInstance?

    /**
     * @param entityRepositories the entities
     * @return the closest entity from where the player is aiming. Null if the entities list is empty.
     */
    fun getClosestEntityToPlayerAim(entityRepositories: List<EntityInstance>): EntityInstance?

    fun getRotationsNeededFromPlayerView(
        target: TargetInstance,
        fovX: Double,
        fovY: Double,
        aimForceX: Double,
        aimForceY: Double
    ): Vec2d

    fun rayTrace(reach: Double, source: Position, direction: Vec2d): BlockInstance?
}

package dev.gallon.aimassistance.domain

interface MinecraftRepository {
    /**
     * @return true if the player is in game and not in any GUIs
     */
    fun isPlayerInGame(): Boolean

    /**
     * @return true if the attack key is being pressed
     */
    fun isAttackKeyPressed(): Boolean

    /**
     * @return true if the player is aiming an entity that is a mob (not a player or an item)
     */
    fun isPlayerAimingMob(): Boolean

    /**
     * TODO
     */
    fun getEntitiesAroundPlayer(range: Double): List<EntityRepository>

    /**
     * @param maxRange the maximum range between the player eyes and the block (unit: 1.0 = 1 block)
     * @return the block the user is looking at if there's one
     */
    fun getPointedBlock(maxRange: Double): BlockRepository?

    /**
     * @param entityRepositories the entities
     * @return the closest entity from where the player is aiming. Null if the entities list is empty.
     */
    fun getClosestEntityToPlayerAim(entityRepositories: List<EntityRepository>): EntityRepository?
}

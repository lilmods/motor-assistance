package dev.gallon.aimassistance.domain

class AimAssistanceService(
    private val minecraftRepository: MinecraftRepository,
    private val aimAssistanceConfig: AimAssistanceConfig
) {

    private var target: TargetRepository? = null
    private var interactingWith: TargetType = TargetType.NONE

    /**
     * This function analyses the player's environment to know what they're aiming at
     */
    fun analyseEnvironment() {
        if (!minecraftRepository.isPlayerInGame()) return

        when (interactingWith) {
            TargetType.ENTITY -> minecraftRepository
                .getEntitiesAroundPlayer(range = aimAssistanceConfig.entityRange)
                .let(minecraftRepository::getClosestEntityToPlayerAim)
                ?.also { entity -> target = entity }

            TargetType.BLOCK -> minecraftRepository
                .getPointedBlock(maxRange = aimAssistanceConfig.blockRange)
                ?.also { block -> target = block }

            TargetType.NONE -> TODO()
        }
    }

    /**
     * This function analyzes the player's behaviour to know if the aim assistance should be turned on or not. It should
     * be called (at least) at every game tick because it uses input events (attack key information).
     */
    fun analyseBehavior() {
        if (!minecraftRepository.isPlayerInGame()) return

        // Mining
        // TODO

        // Attack
        // TODO

        // Common
        // TODO
    }

    /**
     * This function will move the player's aim. The faster this function is called, the smoother the aim assistance is.
     */
    fun assistIfPossible() {
        if (!minecraftRepository.isPlayerInGame()) return
        if (target == null) return

        // TODO
    }
}

package dev.gallon.aimassistance.domain


class AimAssistanceService(
    private val minecraftInstance: MinecraftInstance,
    private val playerInstance: PlayerInstance,
    private val config: AimAssistanceConfig,
    minecraftCallbacks: MinecraftCallbacks
) {

    private var target: TargetInstance? = null
    private var interactingWith: TargetType = TargetType.NONE

    private val interactionTimer = Timer() // Used to assist the player for a given amount of time
    private val miningTimer = Timer() // Used to detect that a player is mining
    private val attackTimer = Timer() // Used to detect that a player is attacking

    private var attackCount = 0

    init {
        minecraftCallbacks.onMouseClick {
            attackCount += 1
            if (attackCount == 1 && config.aimEntity) {
                attackTimer.start()
            } else if (attackCount > 1) {
                // Calculate the number of attacks per milliseconds
                val speed = attackCount.toFloat() / attackTimer.timeElapsed()

                // If player's attack speed is greater than the speed given to toggle the assistance, then we can tell
                // to the instance that the player is interacting
                if (speed > config.attackInteractionSpeed) {
                    miningTimer.stop()

                    // We need to reset the variables that are used to define if the player is interacting because we know
                    // that the user is interacting right now
                    attackCount = 0
                    attackTimer.stop()
                    interactionTimer.start() // it will reset if already started, so we're all good
                    interactingWith = TargetType.ENTITY
                }
            }
        }
    }

    /**
     * This function analyses the player's environment to know what they're aiming at
     */
    fun analyseEnvironment() {
        if (!minecraftInstance.isPlayerInGame()) return

        when (interactingWith) {
            TargetType.ENTITY -> minecraftInstance
                .getEntitiesAroundPlayer(range = config.entityRange)
                .let(minecraftInstance::getClosestEntityToPlayerAim)
                ?.also { entity -> target = entity }

            TargetType.BLOCK -> minecraftInstance
                .getPointedBlock(maxRange = config.blockRange)
                ?.also { block -> target = block }

            TargetType.NONE -> TODO()
        }
    }

    /**
     * This function analyzes the player's behaviour to know if the aim assistance should be turned on or not. It should
     * be called (at least) at every game tick because it uses input events (attack key information).
     */
    fun analyseBehavior() {
        if (!minecraftInstance.isPlayerInGame()) return

        // Common
        val attackKeyPressed = minecraftInstance.attackKeyPressed()
        val interactingWithEntity = this.interactingWith == TargetType.ENTITY
        val interactingWithBlock = this.interactingWith == TargetType.BLOCK

        // Mining
        val playerMiningTimerElapsed = this.miningTimer.timeElapsed(config.miningInteractionDuration)

        if (miningTimer.stopped() && attackKeyPressed && config.aimBlock && !interactingWithEntity) {
            // If the player wasn't doing anything, and is pressing the attack key (= mining), then start the timer
            miningTimer.start()
        } else if (!playerMiningTimerElapsed && !attackKeyPressed && !interactingWithEntity) {
            // Else (means that the player is mining) - if the player stopped mining during the timer, then stop it
            this.miningTimer.stop()
        } else if (playerMiningTimerElapsed && attackKeyPressed) {
            // Else (means that the player is mining) - if the timer is elapsed, then the player is mining
            this.attackTimer.stop()
            this.miningTimer.stop()
            this.interactionTimer.start()
            this.interactingWith = TargetType.BLOCK
        }

        // Attack
        if (!attackTimer.stopped() && attackTimer.timeElapsed(config.attackInteractionSpeed) && !interactingWithBlock) {
            this.attackTimer.stop()
            this.attackCount = 0
        }

        // Common

        // Stop the interaction once that the delay is reached
        val duration = when (interactingWith) {
            TargetType.ENTITY -> config.attackAssistanceDuration
            TargetType.BLOCK -> config.miningAssistanceDuration
            TargetType.NONE -> 0
        }

        if (interactingWith != TargetType.NONE && interactionTimer.timeElapsed(duration)) {
            target = null
            interactingWith = TargetType.NONE
            interactionTimer.stop()
        }
    }

    /**
     * This function will move the player's aim. The faster this function is called, the smoother the aim assistance is.
     */
    fun assistIfPossible() {
        if (!minecraftInstance.isPlayerInGame()) return
        if (target == null) return

        if (interactingWith !== TargetType.NONE && target != null) {
            val aimForce = if (interactingWith === TargetType.BLOCK) config.miningAimForce else config.attackAimForce
            val rotations: Vec2d = minecraftInstance.getRotationsNeededFromPlayerView(
                target!!,
                config.fov, config.fov,
                aimForce, aimForce // forceX, forceY
            )
            var assist = true

            // We need to prevent focusing another block while assisting if the player is not moving his mouse
            val mouseMoved = false // TODO: find a way to do that

            if (interactingWith === TargetType.BLOCK && !mouseMoved) {
                val nextBlock = minecraftInstance.rayTrace(
                    config.blockRange, playerInstance.getEyesPosition(), rotations
                )

                // If, after moving the mouse, another block is focused, then don't assist
                if (nextBlock != null && target != null) {
                    assist =
                        if (target is BlockInstance) {
                            val next = nextBlock.getPosition()
                            val curr = target!!.getPosition()
                            next.x == curr.x && next.y == curr.y && next.z == curr.z
                        } else {
                            false
                        }
                }
            }

            // Don't assist if the option to stop when reached is on AND if the player is currently aiming at a mob
            if (interactingWith === TargetType.ENTITY) {
                assist = !(config.stopAttackOnReached && minecraftInstance.playerAimingMob())
            }
            if (assist) playerInstance.setRotations(rotations)
        }
    }
}

package dev.gallon.aimassistance.domain

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt


class AimAssistanceService(
    private val minecraftInstance: MinecraftInstance,
    private val mouseInstance: MouseInstance,
    private val config: AimAssistanceConfig
) {

    private var target: TargetInstance? = null
    private var interactingWith: TargetType = TargetType.NONE

    private val interactionTimer = Timer() // Used to assist the player for a given amount of time
    private val miningTimer = Timer() // Used to detect that a player is mining
    private val attackTimer = Timer() // Used to detect that a player is attacking

    private var attackCount = 0

    /**
     * This function analyses the player's environment to know what they're aiming at
     */
    fun analyseEnvironment() {
        if (minecraftInstance.getPlayer()?.canInteract() != true) return

        when (interactingWith) {
            TargetType.ENTITY -> minecraftInstance.getPlayer()!!
                .findMobsAroundPlayer(range = config.entityRange)
                .let { entities -> computeClosestEntity(minecraftInstance.getPlayer()!!, entities) }
                ?.also { entity -> target = entity }

            TargetType.BLOCK -> minecraftInstance
                .getPointedBlock(maxRange = config.blockRange)
                ?.also { block -> target = block }

            TargetType.NONE -> {}
        }
    }

    /**
     * This function analyzes the player's behaviour to know if the aim assistance should be turned on or not. It should
     * be called (at least) at every game tick because it uses input events (attack key information).
     */
    fun analyseBehavior() {
        if (minecraftInstance.getPlayer()?.canInteract() != true) return

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

        // Attack detection
        if (mouseInstance.wasLeftClicked()) {
            attackCount += 1
            if (attackCount == 1 && config.aimEntity) {
                attackTimer.start()
            } else if (attackCount > 1) {
                // Calculate the number of attacks per milliseconds
                val speed = attackCount / attackTimer.timeElapsed()

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
        if (minecraftInstance.getPlayer()?.canInteract() != true) return
        if (target == null) return

        if (interactingWith != TargetType.NONE) {
            val aimForce = if (interactingWith === TargetType.BLOCK) config.miningAimForce else config.attackAimForce
            val rotations = computeRotationsNeeded(
                minecraftInstance.getPlayer()!!,
                target!!,
                config.fov, config.fov,
                Rotation(aimForce, aimForce)
            )


            // We need to prevent focusing another block while assisting if the player is not moving his mouse
            val assist = if (interactingWith === TargetType.BLOCK && !mouseInstance.wasMoved()) {
                val nextBlock = minecraftInstance.getPlayer()!!.rayTrace(
                    config.blockRange, minecraftInstance.getPlayer()!!.getEyesPosition(), rotations
                )

                // If, after moving the mouse, another block is focused, then don't assist
                if (nextBlock != null && target != null) {
                    if (target is BlockInstance) {
                        val next = nextBlock.getPosition()
                        val curr = target!!.getPosition()
                        next.x == curr.x && next.y == curr.y && next.z == curr.z
                    } else {
                        false
                    }
                } else {
                    true
                }
            } else if (interactingWith == TargetType.ENTITY) {
                // Don't assist if the option to stop when reached is on AND if the player is currently aiming at a mob
                !(config.stopAttackOnReached && minecraftInstance.playerAimingMob())
            } else {
                true
            }

            if (assist) minecraftInstance.getPlayer()!!.setRotations(rotations)
        }
    }

    /**
     * @param source
     * @param entities
     * @return the closest entity from the source point of view. Null if the entities list is empty.
     */
    private fun computeClosestEntity(source: EntityInstance, entities: List<EntityInstance>): EntityInstance? = entities
        .map { entity -> entity to computeSmallestRotationBetween(source, entity) }
        .minByOrNull { (_, rotation) ->
            val distYaw = abs(wrapDegrees(rotation.yaw - source.getRotations().yaw))
            val distPitch = abs(wrapDegrees(rotation.pitch - source.getRotations().pitch))

            sqrt(distYaw * distYaw + distPitch * distPitch)
        }
        ?.first

    /**
     * @param source
     * @param target
     * @return minimum rotation required to aim the target from the source point of view.
     */
    private fun computeSmallestRotationBetween(source: EntityInstance, target: EntityInstance): Rotation =
        listOf(0.0, 0.05, 0.1, 0.25, 0.5, 0.75, 1.0)
            .map { factor ->
                // The factor is used to aim different parts of the target. 0 means the legs, while 1.0 the eyes

                computeRotationBetween(
                    source.getEyesPosition(),
                    target.getPosition().run { copy(y = y + target.getEyesHeight() * factor) }
                )
            }
            .minBy { rotation -> abs(rotation.yaw) + abs(rotation.pitch) }

    /**
     * @param source
     * @param target
     * @return rotation required to aim the target from the source point in space
     */
    private fun computeRotationBetween(source: Position, target: Position): Rotation = (target - source)
        .let { diff ->
            val dist = sqrt(diff.x * diff.x + diff.z * diff.z)

            Rotation(
                pitch = - (atan2(diff.y, dist) * 180.0 / Math.PI),
                yaw = (atan2(diff.z, diff.x) * 180.0 / Math.PI) - 90.0,
            )
        }

    /**
     * @param source EntityInstance
     * @param target TargetInstance
     * @param fovX Double
     * @param fovY Double
     * @param step Rotation
     * @return Rotation
     */
    private fun computeRotationsNeeded(
        source: EntityInstance,
        target: TargetInstance,
        fovX: Double,
        fovY: Double,
        step: Rotation,
    ): Rotation {

        val rotation = when (target) {
            is BlockInstance -> computeRotationBetween(source.getEyesPosition(), target.getFacePosition())
            is EntityInstance -> computeSmallestRotationBetween(source, target)
        }

        // We check if the entity is within the FOV of the player
        // yaw and pitch are MathHelper.absolute, not relative to anything. We fix that by calling wrapDegrees and subtracting
        // the yaw & pitch to the player's rotation. Now, the yaw, and the pitch are relative to the player's view
        // So we can compare that with the given fov: radiusX, and radiusY (which are both in degrees)
        val inFovX = abs(wrapDegrees(rotation.pitch - source.getRotations().pitch)) * step.pitch <= fovX
        val inFovY = abs(wrapDegrees(rotation.yaw - source.getRotations().yaw)) * step.yaw <= fovY

        // If the targeted entity is within the fov, then, we will compute the step in yaw / pitch of the player's view
        // to get closer to the targeted entity. We will use the given stepX and stepY to compute that. Dividing by 100
        // reduces that step. Without that, we would need to show very low values to the user in the GUI, which is not
        // user-friendly. That way, instead of showing 0.05, we show 5.
        return source.getRotations()
            .run {
                //if (inFovX && inFovY) {
                    copy(
                        yaw = yaw + ((wrapDegrees(rotation.yaw - yaw)) * step.yaw) / 100,
                        pitch = pitch + ((wrapDegrees(rotation.pitch - pitch)) * step.pitch) / 100
                    )
                //} else this
            }
    }
}

fun wrapDegrees(degrees: Double): Double = (degrees % 360.0)
    .let { if (it >= 180.0) it - 360 else it }
    .let { if (it < -180.0) it + 360 else it }

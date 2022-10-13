package dev.gallon.aimassistance.core.domain

data class AimAssistanceConfig(
    // common
    val fov: Double,

    // block
    val aimBlock: Boolean,
    val blockRange: Double,
    val miningInteractionDuration: Long,
    val miningAssistanceDuration: Long,
    val miningAimForce: Double,

    // entity
    val aimEntity: Boolean,
    val entityRange: Double,
    val attackInteractionSpeed: Long,
    val attackInteractionDuration: Long,
    val attackAssistanceDuration: Long,
    val attackAimForce: Double,
    val stopAttackOnReached: Boolean
)

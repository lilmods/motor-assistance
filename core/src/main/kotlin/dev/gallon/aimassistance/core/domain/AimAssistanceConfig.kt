package dev.gallon.aimassistance.core.domain

data class AimAssistanceConfig(
    // common
    val fov: Double = 60.0,

    // block
    val aimBlock: Boolean = true,
    val blockRange: Double = 7.0,
    val miningInteractionDuration: Long = 500,
    val miningAssistanceDuration: Long = 600,
    val miningAimForce: Double = 7.0,

    // entity
    val aimEntity: Boolean = true,
    val entityRange: Double = 5.0,
    val attackInteractionSpeed: Double = 1.5,
    val attackInteractionDuration: Long = 1000,
    val attackAssistanceDuration: Long = 1100,
    val attackAimForce: Double = 7.0,
    val stopAttackOnReached: Boolean = false
)

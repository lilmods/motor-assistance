package dev.gallon.aimassistance.core.domain

data class AimAssistanceConfig(
    // common
    val fov: Double = 60.0,

    // block
    val aimBlock: Boolean = true,
    val blockRange: Double = 7.0,
    val miningInteractionDuration: Long = 500,
    val miningAssistanceDuration: Long = 300,
    val miningAimForce: Double = 2.5,

    // entity
    val aimEntity: Boolean = true,
    val entityRange: Double = 5.0,
    val attackInteractionSpeed: Long = 2,
    val attackInteractionDuration: Long = 2000,
    val attackAssistanceDuration: Long = 1700,
    val attackAimForce: Double = 4.0,
    val stopAttackOnReached: Boolean = false
)

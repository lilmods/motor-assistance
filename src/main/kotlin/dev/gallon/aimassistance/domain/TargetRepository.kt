package dev.gallon.aimassistance.domain

sealed interface TargetRepository {
    fun getPosition(): Position
}

interface EntityRepository : TargetRepository {
    fun getEyeHeight(): Double
}

interface BlockRepository : TargetRepository {
}

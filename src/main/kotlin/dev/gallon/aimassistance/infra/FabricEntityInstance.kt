package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.EntityInstance
import dev.gallon.aimassistance.domain.Position
import net.minecraft.entity.Entity

open class FabricEntityInstance(
    private val entity: Entity
) : EntityInstance {
    override fun getEyesHeight(): Double = with(entity) {
        y + eyeY
    }

    override fun getPosition(): Position = with(entity) {
        Position(x = x, y = y, z = z)
    }
}

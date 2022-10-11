package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.EntityInstance
import dev.gallon.aimassistance.domain.Position
import dev.gallon.aimassistance.domain.Rotation
import net.minecraft.entity.Entity

open class FabricEntityInstance(
    private val entity: Entity
) : EntityInstance {
    override fun getEyesHeight(): Double = with(entity) {
        y + eyeY
    }

    override fun getRotations(): Rotation = with(entity.rotationVector) {
        Rotation(yaw = y, pitch = x)
    }

    override fun getPosition(): Position = with(entity) {
        Position(x = x, y = y, z = z)
    }
}

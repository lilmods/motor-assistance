package dev.gallon.aimassistance.fabric.adapters

import dev.gallon.aimassistance.core.domain.Position
import dev.gallon.aimassistance.core.domain.Rotation
import dev.gallon.aimassistance.core.interfaces.Entity
import net.minecraft.entity.Entity as FabricEntity

open class FabricEntityAdapter(
    private val entity: FabricEntity
) : Entity {
    override fun getEyesHeight(): Double = with(entity) {
        getEyeHeight(pose).toDouble()
    }

    override fun getRotations(): Rotation = with(entity) {
        Rotation(yaw = yaw.toDouble(), pitch = pitch.toDouble())
    }

    override fun getPosition(): Position = with(entity) {
        Position(x = x, y = y, z = z)
    }
}

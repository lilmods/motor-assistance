package dev.gallon.aimassistance.forge.adapters

import dev.gallon.aimassistance.core.domain.Position
import dev.gallon.aimassistance.core.domain.Rotation
import dev.gallon.aimassistance.core.interfaces.Entity
import net.minecraft.world.entity.Entity as ForgeEntity

open class ForgeEntityAdapter(
    private val entity: ForgeEntity,
) : Entity {
    override fun getEyesHeight(): Double = with(entity) {
        getEyeHeight(pose).toDouble()
    }

    override fun getRotations(): Rotation = with(entity) {
        Rotation(yaw = yRot.toDouble(), pitch = xRot.toDouble())
    }

    override fun getPosition(): Position = with(entity) {
        Position(x = x, y = y, z = z)
    }
}

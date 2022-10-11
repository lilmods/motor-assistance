package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.Position
import dev.gallon.aimassistance.domain.Rotation
import net.minecraft.util.math.Vec3d


fun Vec3d.toPosition() = Position(x, y, z)

fun Vec3d.toRotation() = Rotation(yaw = y, pitch = x)


fun Position.toVec3d() = Vec3d(x, y ,z)

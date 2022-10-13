package dev.gallon.aimassistance.fabric.infra

import dev.gallon.aimassistance.fabric.domain.Position
import dev.gallon.aimassistance.fabric.domain.Rotation
import net.minecraft.util.math.Vec3d


fun Vec3d.toPosition() = Position(x, y, z)

fun Vec3d.toRotation() = Rotation(yaw = y, pitch = x)


fun Position.toVec3d() = Vec3d(x, y ,z)

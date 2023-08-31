package dev.gallon.motorassistance.fabric.adapters

import dev.gallon.motorassistance.common.domain.Position
import dev.gallon.motorassistance.common.domain.Rotation
import net.minecraft.util.math.Vec3d

fun Vec3d.toPosition() = Position(x, y, z)

fun Vec3d.toRotation() = Rotation(yaw = y, pitch = x)

fun Position.toVec3d() = Vec3d(x, y, z)

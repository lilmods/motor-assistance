package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.Position
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

fun Entity.getPosition() = Position(x, y, z)

fun Vec3d.toPosition() = Position(x, y, z)

fun Position.toVec3d() = Vec3d(x, y ,z)

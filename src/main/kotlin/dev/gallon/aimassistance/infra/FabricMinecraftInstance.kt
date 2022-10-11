package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.*
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.RaycastContext
import kotlin.math.cos
import kotlin.math.sin

class FabricMinecraftInstance(
    private val minecraft: MinecraftClient
) : MinecraftInstance {

    override fun getPlayer(): PlayerInstance? = minecraft
        .player
        ?.let(::FabricPlayerInstance)

    override fun attackKeyPressed(): Boolean = // TODO
        minecraft.options.attackKey.isPressed

    override fun playerAimingMob(): Boolean =
        minecraft.targetedEntity is MobEntity

    override fun getPointedBlock(maxRange: Double): BlockInstance? =
        minecraft
            .crosshairTarget
            ?.let { target ->
                when (target) {
                    is EntityHitResult -> getPlayer()
                        ?.rayTrace(
                            maxRange,
                            target.entity.eyePos.toPosition(),
                            target.entity.rotationVector.toRotation()
                        )
                    is BlockHitResult -> FabricBlockInstance(target)
                    else -> null
                }
            }
}

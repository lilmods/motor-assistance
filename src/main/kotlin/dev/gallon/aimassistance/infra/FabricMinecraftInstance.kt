package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.*
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.RaycastContext
import kotlin.math.cos
import kotlin.math.sin

class FabricMinecraftInstance(
    private val minecraft: MinecraftClient
) : MinecraftInstance {

    override fun attackKeyPressed(): Boolean =
        minecraft.options.attackKey.isPressed

    override fun playerAimingMob(): Boolean =
        minecraft.targetedEntity is MobEntity

    override fun getPointedBlock(maxRange: Double): BlockInstance? =
        minecraft
            .crosshairTarget
            ?.let {
                if (it.type == HitResult.Type.BLOCK)
                    minecraft.world
                        ?.getBlockEntity(BlockPos(it.pos))
                        ?.let(::FabricBlockInstance)
                else
                    rayTrace(
                        maxRange,
                        minecraft.player!!.eyePos.toPosition(),
                        Vec2d(
                            minecraft.player!!.rotationVector.x, // pitch
                            minecraft.player!!.rotationVector.y // yaw
                        )
                    )
            }

    override fun getRotationsNeededFromPlayerView(
        target: TargetInstance,
        fovX: Double,
        fovY: Double,
        aimForceX: Double,
        aimForceY: Double
    ): Vec2d {
        TODO("Not yet implemented")
    }

    override fun rayTrace(reach: Double, source: Position, direction: Vec2d): BlockInstance? {
        val f2 = cos(- source.y * (Math.PI / 180f) - Math.PI)
        val f3 = sin(- source.y  * (Math.PI / 180f) - Math.PI)
        val f4 = -cos(- source.x * (Math.PI / 180f))
        val f5 = sin(- source.x * (Math.PI / 180f))
        val f6 = f3 * f4
        val f7 = f2 * f4
        val vector3d1 = source.toVec3d().add(f6 * reach, f5 * reach, f7 * reach)

        return minecraft.world
            ?.raycast(
                RaycastContext(
                    source.toVec3d(),
                    vector3d1,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    minecraft.player
                )
            )
            ?.blockPos
            ?.let { minecraft.world!!.getBlockEntity(it) }
            ?.let(::FabricBlockInstance)
    }

}

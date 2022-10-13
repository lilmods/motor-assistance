package dev.gallon.aimassistance.fabric.infra

import dev.gallon.aimassistance.core.*
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RaycastContext

class FabricPlayerInstance(
    private val player: ClientPlayerEntity
) : FabricEntityInstance(player), PlayerInstance {

    override fun setRotation(rotations: Rotation) {
        player.pitch = rotations.pitch.toFloat()
        player.yaw = rotations.yaw.toFloat()
    }

    override fun canInteract(): Boolean = player.canHit()

    override fun findMobsAroundPlayer(range: Double): List<EntityInstance> =
        player
            .world
            ?.getEntitiesByClass(
                MobEntity::class.java,
                Box(
                    player.x - range,
                    player.y - range,
                    player.z - range,
                    player.x + range,
                    player.y + range,
                    player.z + range,
                )
            ) { true }
            ?.map(::FabricEntityInstance)
            ?: emptyList()


    override fun rayTrace(reach: Double, source: Position, direction: Rotation): BlockInstance? {
        val f2 = MathHelper.cos((- direction.yaw * (Math.PI / 180.0) - Math.PI).toFloat())
        val f3 = MathHelper.sin((- direction.yaw * (Math.PI / 180.0) - Math.PI).toFloat())
        val f4 = - MathHelper.cos((- direction.pitch * (Math.PI / 180.0)).toFloat())
        val f5 = MathHelper.sin((- direction.pitch * (Math.PI / 180.0)).toFloat())
        val f6 = f3 * f4
        val f7 = f2 * f4
        val vector = source.toVec3d().add(f6 * reach, f5 * reach, f7 * reach)

        return player.world
            ?.raycast(
                RaycastContext(
                    source.toVec3d(),
                    vector,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player
                )
            )
            ?.let(::FabricBlockInstance)
    }
}

package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.EntityInstance
import dev.gallon.aimassistance.domain.PlayerInstance
import dev.gallon.aimassistance.domain.Position
import dev.gallon.aimassistance.domain.Vec2d
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.Box
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class FabricPlayerInstance(
    private val player: ClientPlayerEntity
) : FabricEntityInstance(player), PlayerInstance {

    override fun setRotations(rotations: Vec2d) {
        player.pitch = rotations.x.toFloat()
        player.yaw = rotations.y.toFloat()
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

    override fun getClosestEntityToPlayerAim(entites: List<EntityInstance>): EntityInstance? = entites
        .map { entity -> entity to getClosestYawPitchBetween(this, entity) }
        .minByOrNull { (_, yawPitch) ->
            val distYaw = abs(Math.toDegrees(yawPitch.y - player.rotationVector.y))
            val distPitch = abs(Math.toDegrees(yawPitch.x - player.rotationVector.x))

            sqrt(distYaw * distYaw + distPitch * distPitch)
        }
        ?.first

    private fun getClosestYawPitchBetween(source: EntityInstance, target: EntityInstance): Vec2d =
        listOf(0.0, 0.05, 0.1, 0.25, 0.5, 0.75, 1.0)
            .map { factor ->
                getYawPitchBetween(
                    source.getPosition(),
                    target.getPosition().run { copy(y = y * factor) }
                )
            }
            .minBy { yawPitch -> abs(yawPitch.x) + abs(yawPitch.y) }

    private fun getYawPitchBetween(source: Position, target: Position): Vec2d = (target - source)
        .let { diff ->
            val dist = sqrt(diff.x * diff.x + diff.z * diff.z)

            Vec2d(
                x = (atan2(diff.z, diff.x) * 180.0 / Math.PI) - 90.0,
                y = -atan2(diff.y, dist) * 180.0 / Math.PI
            )
        }

}

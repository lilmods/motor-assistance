package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.*
import net.minecraft.client.MinecraftClient

class FabricMinecraftInstance(
    private val minecraft: MinecraftClient
) : MinecraftInstance {

    override fun isPlayerInGame(): Boolean {
        TODO("Not yet implemented")
    }

    override fun attackKeyPressed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun playerAimingMob(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getEntitiesAroundPlayer(range: Double): List<EntityInstance> {
        TODO("Not yet implemented")
    }

    override fun getPointedBlock(maxRange: Double): BlockInstance? {
        TODO("Not yet implemented")
    }

    override fun getClosestEntityToPlayerAim(entityRepositories: List<EntityInstance>): EntityInstance? {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

}

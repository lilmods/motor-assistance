package dev.gallon.aimassistance.fabric.infra

import dev.gallon.aimassistance.core.BlockInstance
import dev.gallon.aimassistance.core.MinecraftInstance
import dev.gallon.aimassistance.core.PlayerInstance
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult

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

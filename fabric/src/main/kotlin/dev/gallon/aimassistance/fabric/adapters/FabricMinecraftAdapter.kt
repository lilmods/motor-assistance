package dev.gallon.aimassistance.fabric.adapters

import dev.gallon.aimassistance.core.interfaces.Block
import dev.gallon.aimassistance.core.interfaces.Minecraft
import dev.gallon.aimassistance.core.interfaces.Player
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult

class FabricMinecraftAdapter(
    private val minecraft: MinecraftClient
) : Minecraft {

    override fun getPlayer(): Player? = minecraft
        .player
        ?.let(::FabricPlayerAdapter)

    override fun attackKeyPressed(): Boolean = // TODO
        minecraft.options.attackKey.isPressed

    override fun playerAimingMob(): Boolean =
        minecraft.targetedEntity is MobEntity

    override fun getPointedBlock(maxRange: Double): Block? =
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
                    is BlockHitResult -> FabricBlockAdapter(target)
                    else -> null
                }
            }
}

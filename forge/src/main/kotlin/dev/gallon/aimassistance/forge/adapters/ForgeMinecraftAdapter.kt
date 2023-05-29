package dev.gallon.aimassistance.forge.adapters

import dev.gallon.aimassistance.core.interfaces.Block
import dev.gallon.aimassistance.core.interfaces.Minecraft
import dev.gallon.aimassistance.core.interfaces.Player
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.client.Minecraft as ForgeMinecraft

class ForgeMinecraftAdapter(
    private val minecraft: ForgeMinecraft,
) : Minecraft {

    override fun getPlayer(): Player? = minecraft
        .player
        ?.let(::ForgePlayerAdapter)

    override fun attackKeyPressed(): Boolean =
        minecraft.options.keyAttack.isDown

    override fun playerAimingMob(): Boolean =
        minecraft.crosshairPickEntity is Mob

    override fun getPointedBlock(maxRange: Double): Block? =
        minecraft
            .hitResult
            ?.let { target ->
                when (target) {
                    is EntityHitResult -> getPlayer()
                        ?.rayTrace(
                            maxRange,
                            target.entity.eyePosition.toPosition(),
                            target.entity.lookAngle.toRotation(),
                        )

                    is BlockHitResult -> ForgeBlockAdapter(target)
                    else -> null
                }
            }
}

package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.PlayerInstance
import dev.gallon.aimassistance.domain.Position
import dev.gallon.aimassistance.domain.Vec2d
import net.minecraft.client.network.ClientPlayerEntity

class FabricPlayerInstance(
    private val player: ClientPlayerEntity
) : PlayerInstance {

    override fun setRotations(rotations: Vec2d) {
        TODO("Not yet implemented")
    }

    override fun getEyesHeight(): Double {
        TODO("Not yet implemented")
    }

    override fun getPosition(): Position {
        TODO("Not yet implemented")
    }
}

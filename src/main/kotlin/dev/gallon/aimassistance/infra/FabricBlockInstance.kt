package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.BlockInstance
import dev.gallon.aimassistance.domain.Position
import net.minecraft.block.entity.BlockEntity

class FabricBlockInstance(
    private val block: BlockEntity
) : BlockInstance {
    override fun getPosition(): Position = with(block.pos) {
        Position(x = x.toDouble(), y = y.toDouble(), z = z.toDouble())
    }
}

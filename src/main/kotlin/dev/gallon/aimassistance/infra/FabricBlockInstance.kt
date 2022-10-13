package dev.gallon.aimassistance.infra

import dev.gallon.aimassistance.domain.BlockInstance
import dev.gallon.aimassistance.domain.Position
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction

class FabricBlockInstance(
    private val block: BlockHitResult
) : BlockInstance {
    override fun getFacePosition(): Position = getPosition()
        .run {
            copy(
                x = x.toInt() + 0.5,
                y = y.toInt() + 0.5,
                z = z.toInt() + 0.5
            )
        }
        .run {
            when (block.side) {
                Direction.WEST -> copy(x = x - 0.5)
                Direction.EAST -> copy(x = x + 0.5)
                Direction.NORTH -> copy(z = z - 0.5)
                Direction.SOUTH -> copy(z = z + 0.5)
                Direction.DOWN -> copy(y = y - 0.5)
                Direction.UP -> copy(y = y + 0.5)
                else -> this
            }
        }

    override fun getPosition(): Position = with(block.pos) {
        Position(x = x, y = y, z = z)
    }
}

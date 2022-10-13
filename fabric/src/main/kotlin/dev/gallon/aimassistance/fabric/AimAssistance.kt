package dev.gallon.aimassistance.fabric

import dev.gallon.aimassistance.fabric.domain.AimAssistanceConfig
import dev.gallon.aimassistance.fabric.domain.AimAssistanceService
import dev.gallon.aimassistance.fabric.infra.FabricMinecraftInstance
import dev.gallon.aimassistance.fabric.infra.FabricMouseInstance
import dev.gallon.aimassistance.fabric.infra.events.RenderEvent
import dev.gallon.aimassistance.fabric.infra.events.SingleEventBus
import dev.gallon.aimassistance.fabric.infra.events.TickEvent
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient

class AimAssistance : ModInitializer {

    private var aimAssistance: AimAssistanceService? = null

    override fun onInitialize() {
        SingleEventBus.register<TickEvent> {
            if (aimAssistance == null && MinecraftClient.getInstance().player != null) {
                aimAssistance = AimAssistanceService(
                    minecraftInstance = FabricMinecraftInstance(MinecraftClient.getInstance()),
                    mouseInstance = FabricMouseInstance(MinecraftClient.getInstance().mouse),
                    config = AimAssistanceConfig(
                        // common
                        fov = 60.0,
                        // block
                        aimBlock = true,
                        blockRange = 7.0,
                        miningInteractionDuration = 500,
                        miningAssistanceDuration = 300,
                        miningAimForce = 2.5,
                        // entity
                        aimEntity = true,
                        entityRange = 5.0,
                        attackInteractionSpeed = 1/1000,
                        attackInteractionDuration = 2000,
                        attackAssistanceDuration = 1700,
                        attackAimForce = 4.0,
                        stopAttackOnReached = false,
                    )
                )
            } else if (aimAssistance != null) {
                aimAssistance!!.analyseEnvironment()
                aimAssistance!!.analyseBehavior()

            }
        }

        SingleEventBus.register<RenderEvent> {
            if (aimAssistance != null) {
                aimAssistance!!.assistIfPossible()
            }
        }
    }
}

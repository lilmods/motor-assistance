package dev.gallon.aimassistance

import dev.gallon.aimassistance.domain.AimAssistanceConfig
import dev.gallon.aimassistance.domain.AimAssistanceService
import dev.gallon.aimassistance.infra.FabricMinecraftInstance
import dev.gallon.aimassistance.infra.events.SingleEventBus
import dev.gallon.aimassistance.infra.events.TickEvent
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient

class AimAssistance : ModInitializer {

    var aimAssistance: AimAssistanceService? = null

    override fun onInitialize() {
        SingleEventBus.register<TickEvent> {
            if (aimAssistance == null && MinecraftClient.getInstance().player != null) {
                aimAssistance = AimAssistanceService(
                    minecraftInstance = FabricMinecraftInstance(MinecraftClient.getInstance()),
                    config = AimAssistanceConfig(
                        // common
                        fov = 60.0,
                        // block
                        aimBlock = true,
                        blockRange = 5.0,
                        miningInteractionDuration = 1,
                        miningAssistanceDuration = 5,
                        miningAimForce = 2.5,
                        // entity
                        aimEntity = true,
                        entityRange = 5.0,
                        attackInteractionSpeed = 2,
                        attackAssistanceDuration = 5,
                        attackAimForce = 4.0,
                        stopAttackOnReached = false,
                    )
                )
            } else if (aimAssistance != null) {
                if (MinecraftClient.getInstance().options.attackKey.isPressed) {
                    aimAssistance!!.onMouseClick()
                }

                aimAssistance!!.analyseEnvironment()
                aimAssistance!!.analyseBehavior()
                aimAssistance!!.assistIfPossible()
            }
        }

    }


}

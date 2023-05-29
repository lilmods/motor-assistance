package dev.gallon.aimassistance.fabric

import dev.gallon.aimassistance.core.domain.AimAssistanceConfig
import dev.gallon.aimassistance.core.services.AimAssistanceService
import dev.gallon.aimassistance.fabric.adapters.FabricMinecraftAdapter
import dev.gallon.aimassistance.fabric.adapters.FabricMouseAdapter
import dev.gallon.aimassistance.fabric.config.ModConfig
import dev.gallon.aimassistance.fabric.events.RenderEvent
import dev.gallon.aimassistance.fabric.events.SingleEventBus
import dev.gallon.aimassistance.fabric.events.TickEvent
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient

class AimAssistance : ModInitializer {
    private var aimAssistance: AimAssistanceService? = null

    override fun onInitialize() {
        AutoConfig.register(ModConfig::class.java, ::JanksonConfigSerializer)
        val config = AutoConfig.getConfigHolder(ModConfig::class.java).config

        SingleEventBus.register<TickEvent> {
            initOrResetAimAssistance(config.config)
            aimAssistance?.analyseEnvironment()
            aimAssistance?.analyseBehavior()
        }

        SingleEventBus.register<RenderEvent> {
            aimAssistance?.assistIfPossible()
        }
    }

    private fun initOrResetAimAssistance(config: AimAssistanceConfig) {
        if (aimAssistance == null && MinecraftClient.getInstance().player != null) {
            aimAssistance = AimAssistanceService(
                minecraft = FabricMinecraftAdapter(MinecraftClient.getInstance()),
                mouse = FabricMouseAdapter(),
                config = config,
            )
        } else if (aimAssistance != null && MinecraftClient.getInstance().player == null) {
            aimAssistance = null
        }
    }
}

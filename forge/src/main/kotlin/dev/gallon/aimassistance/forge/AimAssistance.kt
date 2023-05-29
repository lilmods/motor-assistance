package dev.gallon.aimassistance.forge

import dev.gallon.aimassistance.core.services.AimAssistanceService
import dev.gallon.aimassistance.forge.adapters.ForgeMinecraftAdapter
import dev.gallon.aimassistance.forge.adapters.ForgeMouseAdapter
import dev.gallon.aimassistance.forge.config.ModConfig
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod("aimassistance")
object AimAssistance {
    private var aimAssistance: AimAssistanceService? = null

    init {
        AutoConfig.register(ModConfig::class.java, ::JanksonConfigSerializer)
        ModLoadingContext
            .get()
            .registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
                ConfigScreenHandler.ConfigScreenFactory { _: Minecraft?, parent: Screen? ->
                    AutoConfig.getConfigScreen(
                        ModConfig::class.java,
                        parent,
                    ).get()
                }
            }

        FORGE_BUS.addListener(::onLoggingIn)
        FORGE_BUS.addListener(::onLoggingOut)
        FORGE_BUS.addListener(::onPlayerTick)
        FORGE_BUS.addListener(::onClientTick)
        FORGE_BUS.addListener(::onRender)
        FORGE_BUS.register(ForgeMouseAdapter)
    }

    @JvmStatic
    @SubscribeEvent
    fun onLoggingIn(loggingInEvent: LevelEvent.Load) {
        aimAssistance = AimAssistanceService(
            minecraft = ForgeMinecraftAdapter(Minecraft.getInstance()),
            mouse = ForgeMouseAdapter,
            config = AutoConfig.getConfigHolder(ModConfig::class.java).config.config,
        )
    }

    @JvmStatic
    @SubscribeEvent
    fun onLoggingOut(loggingInEvent: LevelEvent.Unload) {
        aimAssistance = null
    }

    @JvmStatic
    @SubscribeEvent
    fun onPlayerTick(playerTickEvent: net.minecraftforge.event.TickEvent.PlayerTickEvent?) {
        aimAssistance?.analyseEnvironment()
    }

    @JvmStatic
    @SubscribeEvent
    fun onClientTick(clientTickEvent: net.minecraftforge.event.TickEvent.ClientTickEvent?) {
        aimAssistance?.analyseEnvironment()
    }

    @JvmStatic
    @SubscribeEvent
    fun onRender(renderTickEvent: net.minecraftforge.event.TickEvent.RenderTickEvent?) {
        aimAssistance?.assistIfPossible()
    }
}

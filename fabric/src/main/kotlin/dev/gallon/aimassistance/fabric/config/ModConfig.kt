package dev.gallon.aimassistance.fabric.config

import dev.gallon.aimassistance.core.domain.AimAssistanceConfig
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "aimassistance")
class ModConfig : ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    val config = AimAssistanceConfig()
}

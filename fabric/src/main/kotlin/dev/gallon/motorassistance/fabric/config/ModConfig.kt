package dev.gallon.motorassistance.fabric.config

import dev.gallon.motorassistance.core.domain.MotorAssistanceConfig
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "motorassistance")
class ModConfig : ConfigData {
    // documentation: https://shedaniel.gitbook.io/cloth-config/auto-config/creating-a-config-class

    @ConfigEntry.Gui.CollapsibleObject
    val config = MotorAssistanceConfig()
}

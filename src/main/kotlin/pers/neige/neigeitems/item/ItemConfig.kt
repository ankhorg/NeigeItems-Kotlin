package pers.neige.neigeitems.item

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ItemConfig(val id: String, val file: File) {
    val configSection = YamlConfiguration.loadConfiguration(file).getConfigurationSection(id)
}
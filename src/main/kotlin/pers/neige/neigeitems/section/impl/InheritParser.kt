package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.section.Section
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object InheritParser : SectionParser() {
    override val id: String = "inherit"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        data.getString("template")?.let { inheritId ->
            val result = when (val section = sections?.getConfigurationSection(inheritId)) {
                null -> sections?.getString(inheritId)?.parseSection(cache, player, sections)
                else -> Section(section).load(cache, player, sections)
            }
            return result
        }
        return null
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        val data = YamlConfiguration()
        data.set("template", args.joinToString("_"))
        return onRequest(data, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}
package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.section.Section
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

// 继承节点解析器
object InheritParser : SectionParser() {
    override val id: String = "inherit"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        // 获取继承模板ID
        data.getString("template")?.let { inheritId ->
            // 获取继承模板
            val result = when (val section = sections?.getConfigurationSection(inheritId)) {
                null -> sections?.getString(inheritId)?.parseSection(cache, player, sections)
                else -> Section(section).get(cache, player, sections)
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
package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.Section
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * 继承节点解析器
 */
object InheritParser : SectionParser() {
    override val id: String = "inherit"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, true, data.getString("template"))
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(cache, player, sections, false, args.joinToString("_")) ?: "<$id::${args.joinToString("_")}>"
    }

    private fun handler(cache: HashMap<String, String>?,
                        player: OfflinePlayer?,
                        sections: ConfigurationSection?,
                        parse: Boolean,
                        template: String?): String? {
        // 获取继承模板ID
        template?.let { inheritId ->
            // 获取继承模板
            val result = when (val section = sections?.getConfigurationSection(inheritId)) {
                // 继承简单节点
                null -> {
                    sections?.getString(inheritId)?.parseSection(parse, cache, player, sections)
                }
                // 继承私有节点
                else -> Section(section, inheritId).get(cache, player, sections)
            }
            return result
        }
        return null
    }
}
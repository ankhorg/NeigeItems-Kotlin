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
        return handler(
            cache,
            player,
            sections,
            data.getString("template")
        )
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(
            cache,
            player,
            sections,
            args.joinToString("_")
        ) ?: "<$id::${args.joinToString("_")}>"
    }


    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param template 继承模板ID文本
     * @return 解析值
     */
    private fun handler(
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        template: String?
    ): String? {
        // 获取继承模板ID
        template?.let {
            // 获取继承模板
            val result = when (val section = sections?.getConfigurationSection(template)) {
                // 继承简单节点
                null -> {
                    sections?.getString(template)?.parseSection(true, cache, player, sections)
                }
                // 继承私有节点
                else -> Section(section, template).get(cache, player, sections)
            }
            return result
        }
        return null
    }
}
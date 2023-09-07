package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser

/**
 * 自定义节点, 具体将在js文件中实现, nashorn会自动解析匹配类型, 因此不用担心重定向问题, 不用将kotlin的function更改为java的function
 * 别问我这个类为什么不叫CustomParser, 等我反应过来的时候为时已晚
 */
class CustomSection(
    // 自定义节点ID
    override val id: String,

    // 用于响应私有节点解析
    private val func1: (
        ConfigurationSection,
        MutableMap<String, String>?,
        OfflinePlayer?,
        ConfigurationSection?
    ) -> String?,

    // 用于响应即时节点解析
    private val func2: (
        List<String>,
        MutableMap<String, String>?,
        OfflinePlayer?,
        ConfigurationSection?
    ) -> String?

) : SectionParser() {

    override fun onRequest(
        data: ConfigurationSection,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?): String? {
        return func1(data, cache, player, sections)
    }

    override fun onRequest(
        args: List<String>,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?): String {
        return func2(args, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}

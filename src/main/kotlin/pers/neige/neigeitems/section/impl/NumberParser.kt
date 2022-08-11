package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * 随机数节点解析器
 */
object NumberParser : SectionParser() {
    override val id: String = "number"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, true, data.getString("min") ,data.getString("max"), data.getString("fixed"))
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(cache, player, sections, false, *args.toTypedArray()) ?: "<$id::${args.joinToString("_")}>"
    }

    private fun handler(cache: HashMap<String, String>?,
                        player: OfflinePlayer?,
                        sections: ConfigurationSection?,
                        parse: Boolean,
                        vararg args: String?): String? {
        // 获取大小范围
        val min = args.getOrNull(0)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        val max = args.getOrNull(1)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        // 获取取整位数
        val fixed = args.getOrNull(2)?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 0
        // 获取随机数
        if (min != null && max != null) {
            return "%.${fixed}f".format(min+(Math.random()*(max-min)))
        }
        return null
    }
}
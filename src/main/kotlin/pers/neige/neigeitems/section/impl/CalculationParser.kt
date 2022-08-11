package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ScriptUtils.eval
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * 公式节点解析器
 */
object CalculationParser : SectionParser() {
    override val id: String = "calculation"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, true, data.getString("formula"), data.getString("fixed"), data.getString("min"), data.getString("max"))
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
        try {
            // 加载公式
            args.getOrNull(0)?.parseSection(parse, cache, player, sections)?.let {
                // 计算结果
                var result = it.eval().toString().toDouble()
                // 获取大小范围
                args.getOrNull(2)?.parseSection(parse, cache, player, sections)?.toDouble()?.let { min ->
                    result = min.coerceAtLeast(result)
                }
                args.getOrNull(3)?.parseSection(parse, cache, player, sections)?.toDouble()?.let { max ->
                    result = max.coerceAtMost(result)
                }
                // 获取取整位数
                val fixed = args.getOrNull(1)?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 0
                // 加载结果
                return "%.${fixed}f".format(result)
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return null
    }
}
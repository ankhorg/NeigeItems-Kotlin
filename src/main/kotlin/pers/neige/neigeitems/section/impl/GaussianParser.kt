package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.*
import kotlin.collections.HashMap

/**
 * gaussian节点解析器
 */
object GaussianParser : SectionParser() {
    private val RANDOM = Random()

    override val id: String = "gaussian"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, true, data.getString("base"), data.getString("spread"), data.getString("maxSpread"), data.getString("fixed"), data.getString("min"), data.getString("max"))
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
        // 获取基础数值
        val base = args.getOrNull(0)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        // 获取浮动单位
        val spread = args.getOrNull(1)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        // 获取浮动范围上限
        val maxSpread = args.getOrNull(2)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        // 获取取整位数
        val fixed = args.getOrNull(3)?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 1
        // 获取大小范围
        val min = args.getOrNull(4)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        val max = args.getOrNull(5)?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        // 获取随机数
        if (base != null && spread != null && maxSpread != null) {
            // 根据正态分布进行范围随机
            val random = (RANDOM.nextGaussian()*spread)
                // 限制随机范围下限
                .coerceAtLeast(-maxSpread)
                // 限制随机范围上限
                .coerceAtMost(maxSpread)
            var result = base*(1 + random)
            // 限制结果下限
            min?.let { result = result.coerceAtLeast(it) }
            // 限制结果上限
            max?.let { result = result.coerceAtMost(it) }
            // 返回结果(基础数值+基础数值*浮动范围)
            return "%.${fixed}f".format(result)
        }
        return null
    }
}
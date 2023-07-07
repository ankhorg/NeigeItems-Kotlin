package pers.neige.neigeitems.section.impl

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.asahi.util.calculate.FormulaParser.calculate
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.ScriptUtils.toRoundingMode
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.math.RoundingMode
import java.util.*

/**
 * 公式节点解析器
 */
object FastCalcParser : SectionParser() {
    override val id: String = "fastcalc"

    override fun onRequest(
        data: ConfigurationSection,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(
            cache,
            player,
            sections,
            true,
            data.getString("formula"),
            data.getString("fixed"),
            data.getString("min"),
            data.getString("max"),
            data.getString("mode")
        )
    }

    override fun onRequest(
        args: List<String>,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(
            cache,
            player,
            sections,
            false,
            args.getOrNull(0),
            args.getOrNull(1),
            args.getOrNull(2),
            args.getOrNull(3),
            args.getOrNull(4)
        ) ?: "<$id::${args.joinToString("_")}>"
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param formulaString 公式文本
     * @param fixedString 取整位数文本
     * @param minString 最小值文本
     * @param maxString 最大值文本
     * @param roundingMode 取整模式
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        formulaString: String?,
        fixedString: String?,
        minString: String?,
        maxString: String?,
        roundingMode: String?
    ): String? {
        try {
            // 加载公式
            formulaString?.parseSection(parse, cache, player, sections)?.let {
                // 计算结果
                var result = it.calculate()
                // 获取大小范围
                minString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()?.let { min ->
                    result = min.coerceAtLeast(result)
                }
                maxString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()?.let { max ->
                    result = max.coerceAtMost(result)
                }
                // 获取取整位数
                val fixed = fixedString?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 0
                // 获取取整模式
                val mode = roundingMode.toRoundingMode()
                // 加载结果
                return result.toBigDecimal().setScale(fixed, mode).toString()
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return null
    }
}
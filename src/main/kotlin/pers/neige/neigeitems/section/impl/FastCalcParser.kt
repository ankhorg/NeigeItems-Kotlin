package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.CalculationUtils.calc
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.math.BigDecimal

/**
 * 公式节点解析器
 */
object FastCalcParser : SectionParser() {
    override val id: String = "fastcalc"

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
            true,
            data.getString("formula"),
            data.getString("fixed"),
            data.getString("min"),
            data.getString("max")
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
            false,
            args.getOrNull(0),
            args.getOrNull(1),
            args.getOrNull(2),
            args.getOrNull(3)
        ) ?: "<$id::${args.joinToString("_")}>"
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param fomulaString 公式文本
     * @param fixedString 取整位数文本
     * @param minString 最小值文本
     * @param maxString 最大值文本
     * @return 解析值
     */
    private fun handler(
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        fomulaString: String?,
        fixedString: String?,
        minString: String?,
        maxString: String?
    ): String? {
        try {
            // 加载公式
            fomulaString?.parseSection(parse, cache, player, sections)?.let {
                // 计算结果
                var result = it.calc()
                // 获取大小范围
                minString?.parseSection(parse, cache, player, sections)?.toDouble()?.let { min ->
                    result = result.max(BigDecimal(min))
                }
                maxString?.parseSection(parse, cache, player, sections)?.toDouble()?.let { max ->
                    result = result.min(BigDecimal(max))
                }
                // 获取取整位数
                val fixed = fixedString?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 0
                // 加载结果
                return "%.${fixed}f".format(result)
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return null
    }
}
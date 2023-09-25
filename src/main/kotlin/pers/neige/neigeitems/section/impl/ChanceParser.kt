package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ScriptUtils.toRoundingMode
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.concurrent.ThreadLocalRandom

/**
 * 概率节点解析器
 */
object ChanceParser : SectionParser() {
    override val id: String = "chance"

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
            data.getString("success"),
            data.getString("total"),
            data.getString("repeat"),
            data.getString("min"),
            data.getString("max")
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
     * @param successString 成功概率文本
     * @param totalString 总概率文本
     * @param repeatString 重复次数文本
     * @param minString 最小值文本
     * @param maxString 最大值文本
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        successString: String?,
        totalString: String?,
        repeatString: String?,
        minString: String?,
        maxString: String?
    ): String? {
        // 获取成功概率
        val successRate = successString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull() ?: 0.0
        // 获取总概率
        val totalRate = totalString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull() ?: 1.0
        // 获取重复次数
        val repeatTimes = repeatString?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 1
        // 获取大小范围
        val min = minString?.parseSection(parse, cache, player, sections)?.toIntOrNull()
        val max = maxString?.parseSection(parse, cache, player, sections)?.toIntOrNull()
        // 成功次数
        var result = 0
        repeat(repeatTimes) {
            if (successRate > ThreadLocalRandom.current().nextDouble(0.0, totalRate)) {
                result += 1
            }
        }
        // 大小范围限制
        if (min != null) {
            result = min.coerceAtLeast(result)
        }
        if (max != null) {
            result = max.coerceAtMost(result)
        }
        return result.toString()
    }
}
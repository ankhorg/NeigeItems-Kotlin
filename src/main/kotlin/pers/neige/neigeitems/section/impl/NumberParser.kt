package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ScriptUtils.toRoundingMode
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.concurrent.ThreadLocalRandom

/**
 * 随机数节点解析器
 */
object NumberParser : SectionParser() {
    override val id: String = "number"

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
            data.getString("min"),
            data.getString("max"),
            data.getString("fixed"),
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
            args.getOrNull(3)
        ) ?: "<$id::${args.joinToString("_")}>"
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param minString 最小值文本
     * @param maxString 最大值文本
     * @param fixedString 取整位数文本
     * @param roundingMode 取整模式
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        minString: String?,
        maxString: String?,
        fixedString: String?,
        roundingMode: String?
    ): String? {
        // 获取大小范围
        val min = minString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        val max = maxString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull()
        // 获取取整位数
        val fixed = fixedString?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 0
        // 获取取整模式
        val mode = roundingMode.toRoundingMode()
        // 获取随机数
        if (min != null && max != null) {
            val num =
                if (min >= max)
                    min
                else
                    ThreadLocalRandom.current().nextDouble(min, max)
            return num.toBigDecimal().setScale(fixed, mode).toString()
        }
        return null
    }
}
package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ScriptUtils.toRoundingMode
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.text.DecimalFormat

/**
 * Format 节点解析器
 */
object FormatParser : SectionParser() {
    override val id: String = "format"

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
            data.getString("value"),
            data.getString("format"),
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
            args.getOrNull(2)
        ) ?: "<$id::${args.joinToString("_")}>"
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param valueString 数字值文本
     * @param formatString 模板文本
     * @param roundingMode 取整模式
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        valueString: String?,
        formatString: String?,
        roundingMode: String?
    ): String? {
        // 获取数字值
        val value = valueString?.parseSection(parse, cache, player, sections)?.toDoubleOrNull() ?: 0
        // 获取数字模板
        val format = DecimalFormat(formatString?.parseSection(parse, cache, player, sections) ?: "#.#")
        // 获取取整模式
        val mode = roundingMode.toRoundingMode()
        // 设置取整模式
        format.roundingMode = mode
        return format.format(value)
    }
}
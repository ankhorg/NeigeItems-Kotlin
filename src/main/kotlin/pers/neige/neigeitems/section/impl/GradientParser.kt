package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ColorUtils
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.StringUtils.joinToString

/**
 * 渐变色节点解析器
 */
object GradientParser : SectionParser() {
    override val id: String = "gradient"

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
            data.getString("colorStart"),
            data.getString("colorEnd"),
            data.getString("step"),
            data.getString("text")
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
            args.joinToString("_", 3)
        ) ?: "<$id::${args.joinToString("_")}>"
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param colorStartString 起始颜色
     * @param colorEndString 结尾颜色
     * @param stepString 每几个字符变一次色
     * @param rawText 文本内容
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        colorStartString: String?,
        colorEndString: String?,
        stepString: String?,
        rawText: String?
    ): String? {
        if (colorStartString == null || colorEndString == null || rawText == null) return null
        val colorStart =
            (colorStartString.parseSection(parse, cache, player, sections).toIntOrNull(16) ?: 0)
                .coerceAtLeast(0)
                .coerceAtMost(0xFFFFFF)
        val colorEnd =
            (colorEndString.parseSection(parse, cache, player, sections).toIntOrNull(16) ?: 0)
                .coerceAtLeast(0)
                .coerceAtMost(0xFFFFFF)

        val step = (stepString?.parseSection(parse, cache, player, sections)?.toIntOrNull() ?: 1)
            .coerceAtLeast(1)

        val text = rawText.parseSection(parse, cache, player, sections)

        if (text.length <= step) {

            return ColorUtils.toHexColorPrefix(colorStart) + text
        }

        val chars = text.toCharArray()
        val result = StringBuilder()

        var redCurrent = ColorUtils.getRed(colorStart)
        var greenCurrent = ColorUtils.getGreen(colorStart)
        var blueCurrent = ColorUtils.getBlue(colorStart)

        if (step == 1) {
            val redStep = (ColorUtils.getRed(colorEnd) - redCurrent) / chars.lastIndex
            val greenStep = (ColorUtils.getGreen(colorEnd) - greenCurrent) / chars.lastIndex
            val blueStep = (ColorUtils.getBlue(colorEnd) - blueCurrent) / chars.lastIndex

            for (char in chars) {
                result.append(ColorUtils.toHexColorPrefix(redCurrent, greenCurrent, blueCurrent))
                result.append(char)
                redCurrent += redStep
                greenCurrent += greenStep
                blueCurrent += blueStep
            }
        } else {
            val redStep = (ColorUtils.getRed(colorEnd) - redCurrent) * step / chars.lastIndex
            val greenStep = (ColorUtils.getGreen(colorEnd) - greenCurrent) * step / chars.lastIndex
            val blueStep = (ColorUtils.getBlue(colorEnd) - blueCurrent) * step / chars.lastIndex

            var current = 1

            for (char in chars) {
                if (current == 1) {
                    result.append(ColorUtils.toHexColorPrefix(redCurrent, greenCurrent, blueCurrent))
                    redCurrent += redStep
                    greenCurrent += greenStep
                    blueCurrent += blueStep
                }
                result.append(char)
                if (current == step) {
                    current = 1
                } else {
                    current++
                }
            }
        }
        return result.toString()
    }
}
package pers.neige.neigeitems.section.impl

import net.md_5.bungee.api.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.StringUtils.joinToString
import java.awt.Color

/**
 * papi节点解析器(仅包含即时声明节点)
 */
object GradientParser : SectionParser() {
    override val id: String = "gradient"

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
            data.getString("colorStart"),
            data.getString("colorEnd"),
            data.getString("step"),
            data.getString("text")
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
     * @param text 文本内容
     * @return 解析值
     */
    private fun handler(
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        colorStartString: String?,
        colorEndString: String?,
        stepString: String?,
        text: String?
    ): String? {
        if (colorStartString != null && colorEndString != null && text != null) {
            val colorStart = Color((colorStartString.toIntOrNull(16) ?: 0)
                .coerceAtLeast(0)
                .coerceAtMost(0xFFFFFF))
            val colorEnd = Color((colorEndString.toIntOrNull(16) ?: 0)
                .coerceAtLeast(0)
                .coerceAtMost(0xFFFFFF))

            val step = (stepString?.toIntOrNull() ?: 1)
                .coerceAtLeast(1)

            if (text.length <= step) {
                return ChatColor.of(colorStart).toString() + text
            }

            val chars = text.toCharArray()
            val result = StringBuilder()

            var redCurrent = colorStart.red
            var greenCurrent = colorStart.green
            var blueCurrent = colorStart.blue

            if (step == 1) {
                val redStep = (colorEnd.red - colorStart.red) / chars.lastIndex
                val greenStep = (colorEnd.green - colorStart.green) / chars.lastIndex
                val blueStep = (colorEnd.blue - colorStart.blue) / chars.lastIndex

                for (char in chars) {
                    result.append(ChatColor.of(Color(redCurrent, greenCurrent, blueCurrent)).toString())
                    result.append(char)
                    redCurrent += redStep
                    greenCurrent += greenStep
                    blueCurrent += blueStep
                }
            } else {
                val redStep = (colorEnd.red - colorStart.red) * step / chars.lastIndex
                val greenStep = (colorEnd.green - colorStart.green) * step / chars.lastIndex
                val blueStep = (colorEnd.blue - colorStart.blue) * step / chars.lastIndex

                var current = 1

                for (char in chars) {
                    if (current == 1) {
                        result.append(ChatColor.of(Color(redCurrent, greenCurrent, blueCurrent)).toString())
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
        return null
    }
}
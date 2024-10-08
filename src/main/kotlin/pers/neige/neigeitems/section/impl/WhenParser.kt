package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.action.ResultType
import pers.neige.neigeitems.manager.ActionManager.parseCondition
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * when节点解析器
 */
object WhenParser : SectionParser() {
    override val id: String = "when"

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
            data.getString("value")?.parseSection(cache, player, sections),
            data.get("conditions")
        )
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param value 待检查文本
     * @param conditions 条件组
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        value: String?,
        conditions: Any?
    ): String? {
        if (conditions !is List<*>) return null
        value?.let { cache?.put("value", value) }
        // 遍历条件
        conditions.forEach { info ->
            // 是Map说明需要条件判断
            if (info is Map<*, *>) {
                // 获取一下条件和结果
                val condition = info["condition"]
                val result = info["result"]
                // 确认类型正确
                if (condition !is String?) return@forEach
                // 如果符合条件
                if (parseCondition(
                        condition, ActionContext(
                            player?.player,
                            mapOf(Pair("value", value), Pair("cache", cache), Pair("sections", sections))
                        )
                    ).type != ResultType.STOP
                ) {
                    // 返回
                    return result.toString().parseSection(cache, player, sections).also { cache?.remove("value") }
                }
            } else {
                // 其他情况说明可以直接返回
                return info.toString().parseSection(cache, player, sections).also { cache?.remove("value") }
            }
        }
        return null
    }
}
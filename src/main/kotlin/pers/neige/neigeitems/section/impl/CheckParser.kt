package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.manager.ActionManager.parseCondition
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.script.CompiledScript
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import javax.script.SimpleBindings

/**
 * check节点解析器
 */
object CheckParser : SectionParser() {
    override val id: String = "check"

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
            data.getString("value")?.parseSection(cache, player, sections),
            data.get("actions")
        )
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param value 待检查文本
     * @param actions 动作
     * @return 解析值
     */
    private fun handler(
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        value: String?,
        actions: Any?
    ): String? {
        player?.player?.let {
            ActionManager.runAction(
                it,
                actions,
                null,
                null,
                null,
                mapOf(
                    Pair("value", value),
                    Pair("cache", cache),
                    Pair("sections", sections)
                )
            )
        }
        return value
    }
}
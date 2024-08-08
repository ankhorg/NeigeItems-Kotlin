package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.getSection
import pers.neige.neigeitems.utils.StringUtils.joinToString

/**
 * 默认节点解析器
 */
object DefaultParser : SectionParser() {
    override val id: String = "default"

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
            data.getString("key"),
            data.getString("default")
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
            args.getOrNull(0),
            args.getOrNull(1)
        ) ?: "<$id::${args.joinToString("_")}>"
    }


    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param key 待检测key
     * @param default 默认值
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        key: String?,
        default: String?
    ): String? {
        key?.getSection(cache, player, sections)
        return cache?.getOrDefault(key, default) ?: default
    }
}
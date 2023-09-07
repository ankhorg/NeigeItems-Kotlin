package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager.requestPapi
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.StringUtils.joinToString

/**
 * papi节点解析器(仅包含即时声明节点)
 */
object PapiParser : SectionParser() {
    override val id: String = "papi"

    override fun onRequest(
        args: List<String>,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        // 相较于papi(player, "%${args.joinToString("_")}%"), 这种方式性能略有提升, 因为少遍历了一次字符串
        return player?.let {
            requestPapi(player, args[0], args.joinToString("_", 1))
        } ?: "<$id::${args.joinToString("_")}>"
    }
}
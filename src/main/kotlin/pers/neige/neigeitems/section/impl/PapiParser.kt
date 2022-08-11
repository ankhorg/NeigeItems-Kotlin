package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * papi节点解析器(仅包含即时声明节点)
 */
object PapiParser : SectionParser() {
    override val id: String = "papi"

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return player?.let { papi(player, "%${args.joinToString("_")}%").parseSection(cache, player, sections) } ?: "<$id::${args.joinToString("_")}>"
    }
}
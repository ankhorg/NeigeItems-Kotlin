package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object StringsParser : SectionParser() {
    override val id: String = "strings"

    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        // 加载字符串组
        when (val strings = data["values"]) {
            is List<*> -> {
                strings.shuffled().take(1).forEach {
                    return it.toString().parseSection(cache, player, sections)
                }
            }
        }
        return null
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        val data = HashMap<String, Any>()
        data["values"] = args
        return onRequest(data, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}
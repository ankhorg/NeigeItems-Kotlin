package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object StringsParser : SectionParser() {
    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        // 如果配置了字符串组
        if (data.containsKey("values")) {
            // 加载字符串组
            val strings = data["values"] as ArrayList<*>
            strings.shuffled().take(1).forEach {
                return (it as String).parseSection(cache, player, sections)
            }
        }
        return null
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        val data = HashMap<String, Any>()
        data["values"] = args
        return onRequest(data, cache, player, sections) ?: "<strings::${args.joinToString("_")}>"
    }
}
package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser

// kotlin 的重定向在 nashorn 面前不如一根**，所以不用特意把 () -> String? 转成 Function 或者 Consumer 之类的
class CustomSection(override val id: String,
                    private val func1: (HashMap<String, *>, HashMap<String, String>?, OfflinePlayer?, ConfigurationSection?) -> String?,
                    private val func2: (List<String>, HashMap<String, String>?, OfflinePlayer?, ConfigurationSection?) -> String?) : SectionParser() {
    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        return func1(data, cache, player, sections)
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        return func2(args, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}

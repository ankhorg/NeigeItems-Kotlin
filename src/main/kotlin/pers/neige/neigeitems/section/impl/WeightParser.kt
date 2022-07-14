package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object WeightParser : SectionParser() {
    override val id: String = "weight"

    override fun onRequest(data: ConfigurationSection, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        // 加载字符串组
        val strings = ArrayList<String>()
        data.getStringList("values").forEach {
            val value = it.toString().parseSection(cache, player, sections)
            when (val index = value.indexOf("::")) {
                -1 -> {
                    strings.add(value)
                }
                else -> {
                    val weight = value.substring(0, index).toIntOrNull() ?: 1
                    val string = value.substring(index+2, value.length)
                    for (i in 1..weight) strings.add(string)
                }
            }
        }
        return strings[(0 until strings.size).random()].parseSection(cache, player, sections)
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        val data = YamlConfiguration()
        data.set("values", args)
        return onRequest(data, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}
package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object NumberParser : SectionParser() {
    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        // 如果配置了数字范围
        try {
            // 获取大小范围
            val min = data["min"]?.toString()?.parseSection(cache, player, sections)?.toDouble()
            val max = data["max"]?.toString()?.parseSection(cache, player, sections)?.toDouble()
            // 获取取整位数
            val fixed = data["fixed"]?.toString()?.parseSection(cache, player, sections)?.toInt() ?: 0
            // 加载随机数
            if (min != null && max != null) {
                return "%.${fixed}f".format(min+(Math.random()*(max-min)))
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return null
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        val data = HashMap<String, Any>()
        val size = args.size
        if (size > 0) data["min"] = args[0]
        if (size > 1) data["max"] = args[1]
        if (size > 2) data["fixed"] = args[2]
        return onRequest(data, cache, player, sections) ?: "<number::${args.joinToString("_")}>"
    }
}
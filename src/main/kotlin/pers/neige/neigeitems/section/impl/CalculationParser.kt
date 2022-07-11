package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ScriptUtils.eval
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object CalculationParser : SectionParser() {
    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        // 如果配置了数字范围
        try {
            // 计算结果
            var result = data["formula"].toString().parseSection(cache, player, sections).eval()
            result = result.toString().toDouble()
            // 获取大小范围
            val min = data["min"]?.toString()?.parseSection(cache, player, sections)?.toDouble()
            min?.let {
                result = it.coerceAtLeast(result as Double)
            }
            val max = data["max"]?.toString()?.parseSection(cache, player, sections)?.toDouble()
            max?.let {
                result = it.coerceAtMost(result as Double)
            }
            // 获取取整位数
            val fixed = data["fixed"]?.toString()?.parseSection(cache, player, sections)?.toInt() ?: 0
            // 加载结果
            return "%.${fixed}f".format(result)
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return null
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        val data = HashMap<String, Any>()
        val size = args.size
        if (size > 0) data["formula"] = args[0]
        if (size > 1) data["fixed"] = args[1]
        if (size > 2) data["min"] = args[2]
        if (size > 3) data["max"] = args[3]
        return onRequest(data, cache, player, sections) ?: "<number::${args.joinToString("_")}>"
    }
}
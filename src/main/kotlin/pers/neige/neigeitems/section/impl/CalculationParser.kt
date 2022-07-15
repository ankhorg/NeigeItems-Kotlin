package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.ScriptUtils.eval
import pers.neige.neigeitems.utils.SectionUtils.parseSection

object CalculationParser : SectionParser() {
    override val id: String = "calculation"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        // 如果配置了数字范围
        try {
            // 计算结果
            data.getString("formula")?.parseSection(cache, player, sections)?.let {
                var result = it.eval().toString().toDouble()
                // 获取大小范围
                data.getString("min")?.parseSection(cache, player, sections)?.toDouble()?.let { min ->
                    result = min.coerceAtLeast(result)
                }
                data.getString("max")?.parseSection(cache, player, sections)?.toDouble()?.let { max ->
                    result = max.coerceAtMost(result)
                }
                // 获取取整位数
                val fixed = data["fixed"]?.toString()?.parseSection(cache, player, sections)?.toIntOrNull() ?: 0
                // 加载结果
                return "%.${fixed}f".format(result)
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return null
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        val data = YamlConfiguration()
        val size = args.size
        if (size > 0) data.set("formula", args[0])
        if (size > 1) data.set("fixed", args[1])
        if (size > 2) data.set("min", args[2])
        if (size > 3) data.set("max", args[3])
        return onRequest(data, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}
package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

// 权重节点解析器
object WeightParser : SectionParser() {
    override val id: String = "weight"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?, sections:
        ConfigurationSection?
    ): String? {
        val strings = ArrayList<String>()
        // 加载所有参数并遍历
        data.getStringList("values").forEach {
            val value = it.toString().parseSection(cache, player, sections)
            // 检测权重
            when (val index = value.indexOf("::")) {
                // 无权重, 直接记录
                -1 -> {
                    strings.add(value)
                }
                // 有权重, 根据权重大小进行记录
                else -> {
                    val weight = value.substring(0, index).toIntOrNull() ?: 1
                    val string = value.substring(index+2, value.length)
                    for (i in 1..weight) strings.add(string)
                }
            }
        }
        // 根据最后的记录值进行字符随机
        return when {
            strings.isEmpty() -> null
            else -> strings[(0 until strings.size).random()].parseSection(cache, player, sections)
        }
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?, sections:
        ConfigurationSection?
    ): String {
        val data = YamlConfiguration()
        data.set("values", args)
        return onRequest(data, cache, player, sections) ?: "<$id::${args.joinToString("_")}>"
    }
}
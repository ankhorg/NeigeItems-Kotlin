package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.math.BigDecimal

// 权重节点解析器
object WeightParser : SectionParser() {
    override val id: String = "weight"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?, sections:
        ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, true, data.getStringList("values"))
    }

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(cache, player, sections, false, args) ?: "<${StringsParser.id}::${args.joinToString("_")}>"
    }

    private fun handler(cache: HashMap<String, String>?,
                        player: OfflinePlayer?,
                        sections: ConfigurationSection?,
                        parse: Boolean,
                        values: List<String>): String? {
        val info = HashMap<String, BigDecimal>()
        var total = BigDecimal(0)
        // 加载所有参数并遍历
        values.forEach {
            val value = it.toString().parseSection(parse, cache, player, sections)
            // 检测权重
            when (val index = value.indexOf("::")) {
                // 无权重, 直接记录
                -1 -> {
                    info[value]?.let {
                        info[value] = it.add(BigDecimal(1))
                    } ?: let { info[value] = BigDecimal(1) }
                    total = total.add(BigDecimal(1))
                }
                // 有权重, 根据权重大小进行记录
                else -> {
                    val weight = value.substring(0, index).toLongOrNull() ?: 1
                    val string = value.substring(index+2, value.length)
                    info[string]?.let {
                        info[string] = it.add(BigDecimal(weight))
                    } ?: let { info[string] = BigDecimal(weight) }
                    total = total.add(BigDecimal(weight))
                }
            }
        }
        // 根据最后的记录值进行字符随机
        return when {
            info.isEmpty() -> null
            else -> {
                val random = BigDecimal(Math.random().toString()).multiply(total)
                var current = BigDecimal(0)
                var result: String? = null
                for ((key, value) in info) {
                    current = current.add(value)
                    if (random <= current) {
                        result = key.parseSection(parse, cache, player, sections)
                        break
                    }
                }
                result
            }
        }
    }
}
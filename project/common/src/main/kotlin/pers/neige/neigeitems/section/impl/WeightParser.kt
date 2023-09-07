package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.math.BigDecimal
import java.util.concurrent.ThreadLocalRandom

/**
 * 权重节点解析器
 */
object WeightParser : SectionParser() {
    override val id: String = "weight"

    override fun onRequest(
        data: ConfigurationSection,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?, sections:
        ConfigurationSection?
    ): String? {
        return handler(
            cache,
            player,
            sections,
            true,
            data.getStringList("values")
        )
    }

    override fun onRequest(
        args: List<String>,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(
            cache,
            player,
            sections,
            false,
            args
        ) ?: "<$id::${args.joinToString("_")}>"
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param values 文本列表
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        values: List<String>
    ): String? {
        val info = HashMap<String, BigDecimal>()
        var total = BigDecimal.ZERO
        // 加载所有参数并遍历
        values.forEach {
            val value = it.parseSection(parse, cache, player, sections)
            // 检测权重
            when (val index = value.indexOf("::")) {
                // 无权重, 直接记录
                -1 -> {
                    info[value]?.let {
                        info[value] = it.add(BigDecimal.ONE)
                    } ?: let { info[value] = BigDecimal.ONE }
                    total = total.add(BigDecimal.ONE)
                }
                // 有权重, 根据权重大小进行记录
                else -> {
                    val weight = value.substring(0, index).toBigDecimalOrNull() ?: BigDecimal.ONE
                    val string = value.substring(index+2, value.length)
                    info[string]?.let {
                        info[string] = it.add(weight)
                    } ?: let { info[string] = weight }
                    total = total.add(weight)
                }
            }
        }
        // 根据最后的记录值进行字符随机
        return when {
            info.isEmpty() -> null
            else -> {
                val random = BigDecimal(ThreadLocalRandom.current().nextDouble().toString()).multiply(total)
                var current = BigDecimal.ZERO
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
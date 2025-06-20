package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SamplingUtils.weight
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * 可重复权重声明节点解析器
 */
object RepeatableWeightDeclareParser : SectionParser() {
    override val id: String = "rweightdeclare"

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
            data.getStringList("list"),
            data.getString("key"),
            data.getString("amount"),
            data.getString("shuffled"),
            data.getString("order")
        )
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param list 文本列表
     * @param rawKey 节点键
     * @param rawAmount 声明数量
     * @param rawShuffled 是否乱序
     * @param rawPutElse 是否记录未选中内容
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        list: List<String>,
        rawKey: String?,
        rawAmount: String?,
        rawShuffled: String?,
        rawOrder: String?
    ): String? {
        // 获取是否乱序
        val shuffled = rawShuffled?.parseSection(cache, player, sections)?.toBooleanStrictOrNull() ?: false
        // 获取是否顺序
        val order = rawOrder?.parseSection(cache, player, sections)?.toBooleanStrictOrNull() ?: false
        // 索引记录
        val indexMap = if (order) {
            HashMap<String, Int>()
        } else {
            null
        }

        val info = ArrayList<Pair<String, Double>>()
        // 加载所有参数并遍历
        list.forEachIndexed { i, it ->
            val value = it.parseSection(cache, player, sections)
            // 检测权重
            when (val index = value.indexOf("::")) {
                // 无权重, 直接记录
                -1 -> {
                    info.add(value to 1.0)
                    // 索引记录
                    indexMap?.put(value, i)
                }
                // 有权重, 根据权重大小进行记录
                else -> {
                    val weight = value.substring(0, index).toDoubleOrNull() ?: 1.0
                    val string = value.substring(index + 2, value.length)
                    info.add(string to weight)
                    // 索引记录
                    indexMap?.put(string, i)
                }
            }
        }

        // 获取声明节点键
        val key = rawKey?.parseSection(cache, player, sections)

        // 获取声明数量
        val originAmount = rawAmount?.parseSection(cache, player, sections)?.toIntOrNull()?.let {
            when {
                it >= info.size -> info.size
                it < 0 -> 0
                else -> it
            }
        } ?: 1
        val amount = originAmount

        // 获取结果
        val realList = when {
            shuffled -> weight(info, amount).shuffled()
            order -> weight(info, amount).sortedBy { indexMap!![it] }
            else -> weight(info, amount)
        }

        if (key != null && cache != null) {
            var length = amount
            // 当前声明的索引值
            var index = 0
            // 列表元素索引值
            var realIndex = 0
            // 循环
            while (index < length) {
                // 如果对应位置已有节点
                if (cache.contains("$key.$index")) {
                    // 延长循环
                    length++
                    // 对应位置没有节点
                } else {
                    cache["$key.$index"] = realList[realIndex]
                    realIndex++
                }
                index++
            }
            cache["$key.length"] = originAmount.toString()
        }

        return cache?.get("$key.0")
    }
}
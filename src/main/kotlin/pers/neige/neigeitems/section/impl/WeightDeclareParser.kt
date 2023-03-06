package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SamplingUtils.aExpj
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * 权重声明节点解析器
 */
object WeightDeclareParser : SectionParser() {
    override val id: String = "weightdeclare"

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
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
            data.getString("putelse")
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
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        list: List<String>,
        rawKey: String?,
        rawAmount: String?,
        rawShuffled: String?,
        rawPutElse: String?
    ): String? {
        val info = HashMap<String, Double>()
        // 加载所有参数并遍历
        list.forEach {
            val value = it.parseSection(cache, player, sections)
            // 检测权重
            when (val index = value.indexOf("::")) {
                // 无权重, 直接记录
                -1 -> {
                    info[value]?.let {
                        info[value] = it + 1
                    } ?: let { info[value] = 1.0 }
                }
                // 有权重, 根据权重大小进行记录
                else -> {
                    val weight = value.substring(0, index).toDoubleOrNull() ?: 1.0
                    val string = value.substring(index+2, value.length)
                    info[string]?.let {
                        info[string] = it + weight
                    } ?: let { info[string] = weight }
                }
            }
        }

        // 获取声明数量
        val amount = rawAmount?.parseSection(cache, player, sections)?.toIntOrNull()?.let {
            when {
                it >= info.size -> info.size
                it < 0 -> 0
                else -> it
            }
        } ?: 1

        // 获取是否乱序
        val shuffled = rawShuffled?.parseSection(cache, player, sections)?.toBooleanStrictOrNull() ?: false
        val realList = if (shuffled) {
            aExpj(info, amount).shuffled()
        } else {
            aExpj(info, amount)
        }

        // 是否记录未选中内容
        val putElse = rawPutElse?.parseSection(cache, player, sections)?.toBooleanStrictOrNull() ?: false

        // 获取声明节点键
        val key = rawKey?.parseSection(cache, player, sections)

        if (key != null && cache != null) {
            for (index in realList.indices) {
                cache.putIfAbsent("$key.$index", realList[index])
            }
            cache.putIfAbsent("$key.length", realList.size.toString())

            if (putElse) {
                val elseList = info.keys.also { it.removeAll(realList) }
                var index = 0
                elseList.forEach { element ->
                    cache.putIfAbsent("$key.else.$index", element)
                    index ++
                }
                cache.putIfAbsent("$key.else.length", elseList.size.toString())
            }
        }

        return realList.getOrNull(0)
    }
}
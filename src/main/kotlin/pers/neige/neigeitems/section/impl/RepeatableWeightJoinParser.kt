package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.script.CompiledScript
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SamplingUtils.aExpj
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.concurrent.ConcurrentHashMap

/**
 * repeatableweightjoin节点解析器
 */
object RepeatableWeightJoinParser : SectionParser() {
    override val id: String = "rweightjoin"

    /**
     * 获取所有用于rweightjoin节点的已编译的js脚本文件及文本
     */
    val compiledScripts = ConcurrentHashMap<String, CompiledScript>()

    override fun onRequest(
        data: ConfigurationSection,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(
            cache,
            player,
            sections,
            data.getStringList("list"),
            data.getString("separator"),
            data.getString("prefix"),
            data.getString("postfix"),
            data.getString("amount"),
            data.getString("transform"),
            data.getString("shuffled"),
            data.getString("order")
        )
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param list 待操作列表
     * @param rawSeparator 分隔符
     * @param rawPrefix 前缀
     * @param rawPostfix 后缀
     * @param rawAmount 选取数量
     * @param rawTransform 操作函数
     * @param rawShuffled 是否乱序
     * @param rawOrder 是否按原有顺序排列
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        list: List<String>?,
        rawSeparator: String?,
        rawPrefix: String?,
        rawPostfix: String?,
        rawAmount: String?,
        rawTransform: String?,
        rawShuffled: String?,
        rawOrder: String?
    ): String? {
        // 如果待操作列表存在, 进行后续操作
        list?.let {
            // 获取分隔符(默认为", ")
            val separator = rawSeparator?.parseSection(cache, player, sections) ?: ", "
            // 获取前缀
            val prefix = rawPrefix?.parseSection(cache, player, sections) ?: ""
            // 获取后缀
            val postfix = rawPostfix?.parseSection(cache, player, sections) ?: ""
            // 获取操作函数
            val transform = rawTransform?.let {
                compiledScripts.computeIfAbsent(it) {
                    CompiledScript(
                        """
                        function main() {
                            $it
                        }""".trimIndent()
                    )
                }
            }
            // 获取是否乱序
            val shuffled = rawShuffled?.parseSection(cache, player, sections)?.toBooleanStrictOrNull() ?: false
            // 获取是否顺序
            val order = rawOrder?.parseSection(cache, player, sections)?.toBooleanStrictOrNull() ?: false

            // 开始构建结果
            val result = StringBuilder()
            // 添加前缀
            result.append(prefix)
            // 索引记录
            val indexMap = if (order) {
                HashMap<String, Int>()
            } else {
                null
            }

            // 加权随机取值
            val info = ArrayList<Pair<String, Double>>()
            // 加载所有参数并遍历
            list.forEachIndexed { i, raw ->
                val value = raw.parseSection(cache, player, sections)
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

            // 获取数量限制
            val amount = rawAmount?.parseSection(cache, player, sections)?.toIntOrNull()?.let {
                when {
                    it >= info.size -> info.size
                    it < 0 -> 0
                    else -> it
                }
            } ?: 1

            // 获取结果
            val realList = when {
                shuffled -> aExpj(info, amount).shuffled()
                order -> aExpj(info, amount).sortedBy { indexMap!![it] }
                else -> aExpj(info, amount)
            }

            // 预定义参数map
            val map = HashMap<String, Any>()
            // 预添加参数
            transform?.let {
                player?.let {
                    // 玩家
                    map["player"] = player
                }
                // 待操作列表
                map["list"] = realList
                // 节点解析函数
                map["vars"] = java.util.function.Function<String, String> { string ->
                    string.parseSection(
                        cache,
                        player,
                        sections
                    )
                }
            }

            // 遍历列表
            for (index in realList.indices) {
                // 解析元素节点
                var element = realList[index]
                // 操作元素
                transform?.let {
                    // 待操作元素
                    map["it"] = element
                    // 当前序号
                    map["index"] = index
                    // 操作元素
                    element = transform.invoke("main", map)?.toString() ?: ""
                }
                // 添加元素
                result.append(element)
                // 添加分隔符
                if (index != (realList.size - 1)) {
                    result.append(separator)
                }
            }
            // 添加后缀
            result.append(postfix)
            // 返回结果
            return result.toString()
        }
        return null
    }
}
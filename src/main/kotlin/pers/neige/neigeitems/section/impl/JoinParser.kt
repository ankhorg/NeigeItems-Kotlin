package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.script.CompiledScript
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.lang.StringBuilder
import java.util.concurrent.ConcurrentHashMap

/**
 * join节点解析器
 */
object JoinParser : SectionParser() {
    override val id: String = "join"

    /**
     * 所有用于join节点的已编译的js脚本文件及文本
     */
    val compiledScripts = ConcurrentHashMap<String, CompiledScript>()

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, data.getStringList("list"), data.getString("separator"), data.getString("prefix"), data.getString("postfix"), data.getString("limit"), data.getString("truncated"), data.getString("transform"))
    }

    private fun handler(cache: HashMap<String, String>?,
                        player: OfflinePlayer?,
                        sections: ConfigurationSection?,
                        list: List<String>?,
                        rawSeparator: String?,
                        rawPrefix: String?,
                        rawPostfix: String?,
                        rawLimit: String?,
                        rawTruncated: String?,
                        rawTransform: String?,
                        ): String? {
        // 如果待操作列表存在, 进行后续操作
        list?.let {
            // 获取分隔符(默认为", ")
            val separator = rawSeparator?.parseSection(cache, player, sections) ?: ", "
            // 获取前缀
            val prefix = rawPrefix?.parseSection(cache, player, sections) ?: ""
            // 获取后缀
            val postfix = rawPostfix?.parseSection(cache, player, sections) ?: ""
            // 获取长度限制
            val limit = rawLimit?.parseSection(cache, player, sections)?.toIntOrNull()?.let {
                when {
                    it >= list.size -> null
                    it < 0 -> 0
                    else -> it
                }
            }
            // 获取删节符号
            val truncated = rawTruncated?.parseSection(cache, player, sections)
            // 获取操作函数
            val transform = rawTransform?.let {
                compiledScripts.computeIfAbsent(it) {
                    CompiledScript("""
                        function main() {
                            $it
                        }""".trimIndent())
                }
            }

            // 开始构建结果
            val result = StringBuilder()
            // 添加前缀
            result.append(prefix)
            // 遍历列表
            val length = limit ?: list.size

            // 预定义参数map
            val map = HashMap<String, Any>()
            // 预添加参数
            transform?.let {
                player?.let {
                    // 玩家
                    map["player"] = player
                }
                // 待操作列表
                map["list"] = list
                // 节点解析函数
                map["vars"] = java.util.function.Function<String, String> { string -> string.parseSection(cache, player, sections) }
            }

            for (index in 0 until length) {
                // 解析元素节点
                var element = list[index].parseSection(cache, player, sections)
                // 操作元素
                transform?.let {
                    // 待操作元素
                    map["it"] = element
                    // 当前序号
                    map["index"] = index
                    // 操作元素
                    element = transform.invoke("main", map)?.toString()?.parseSection(cache, player, sections) ?: ""
                }
                // 添加元素
                result.append(element)
                // 添加分隔符
                if (index != (length - 1) || (limit != null && truncated != null)) {
                    result.append(separator)
                }
            }
            // 添加删节符号
            if (limit != null && truncated != null) {
                result.append(truncated)
            }
            // 添加后缀
            result.append(postfix)
            // 返回结果
            return result.toString()
        }
        return null
    }
}
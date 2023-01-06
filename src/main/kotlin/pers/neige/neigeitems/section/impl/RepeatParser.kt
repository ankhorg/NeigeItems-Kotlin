package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.script.CompiledScript
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.lang.StringBuilder
import java.util.concurrent.ConcurrentHashMap

/**
 * repeat节点解析器
 */
object RepeatParser : SectionParser() {
    override val id: String = "repeat"

    /**
     * 所有用于repeat节点的已编译的js脚本文件及文本
     */
    val compiledScripts = ConcurrentHashMap<String, CompiledScript>()

    override fun onRequest(
        data: ConfigurationSection,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(cache, player, sections, data.getString("content"), data.getString("separator"), data.getString("prefix"), data.getString("postfix"), data.getString("repeat"), data.getString("transform"))
    }

    private fun handler(cache: HashMap<String, String>?,
                        player: OfflinePlayer?,
                        sections: ConfigurationSection?,
                        rawContent: String?,
                        rawSeparator: String?,
                        rawPrefix: String?,
                        rawPostfix: String?,
                        rawRepeat: String?,
                        rawTransform: String?,
    ): String {
        // 获取待重复内容
        val content = rawContent?.parseSection(cache, player, sections) ?: ""
        // 获取分隔符(默认为"")
        val separator = rawSeparator?.parseSection(cache, player, sections)
        // 获取前缀
        val prefix = rawPrefix?.parseSection(cache, player, sections)
        // 获取后缀
        val postfix = rawPostfix?.parseSection(cache, player, sections)
        // 获取长度限制
        val repeat = (rawRepeat?.parseSection(cache, player, sections)?.toIntOrNull() ?: 1).coerceAtLeast(0)
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
        prefix?.let {
            result.append(it)
        }

        // 预定义参数map
        val map = HashMap<String, Any>()
        // 预添加参数
        transform?.let {
            player?.let {
                // 玩家
                map["player"] = player
            }
            // 待操作元素
            map["it"] = content
            // 节点解析函数
            map["vars"] = java.util.function.Function<String, String> { string -> string.parseSection(cache, player, sections) }
        }

        for (index in 0 until repeat) {
            // 解析元素节点
            var element = content
            // 操作元素
            transform?.let {
                // 当前序号
                map["index"] = index
                // 操作元素
                element = transform.invoke("main", map)?.toString()?.parseSection(cache, player, sections) ?: ""
            }
            // 添加元素
            result.append(element)
            // 添加分隔符
            separator?.let {
                if (index != (repeat - 1)) {
                    result.append(it)
                }
            }
        }
        // 添加后缀
        postfix?.let {
            result.append(it)
        }
        // 返回结果
        return result.toString()
    }
}
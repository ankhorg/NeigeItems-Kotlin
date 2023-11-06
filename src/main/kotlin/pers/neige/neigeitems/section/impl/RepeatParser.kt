package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.script.CompiledScript
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.concurrent.ConcurrentHashMap

/**
 * repeat节点解析器
 */
object RepeatParser : SectionParser() {
    override val id: String = "repeat"

    /**
     * 获取所有用于repeat节点的已编译的js脚本文件及文本
     */
    val compiledScripts = ConcurrentHashMap<String, CompiledScript>()

    override fun onRequest(
        data: ConfigurationSection,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(
            cache,
            player,
            sections,
            data.getString("content")?.parseSection(cache, player, sections) ?: "",
            data.getString("separator")?.parseSection(cache, player, sections),
            data.getString("prefix")?.parseSection(cache, player, sections),
            data.getString("postfix")?.parseSection(cache, player, sections),
            data.getString("repeat")?.parseSection(cache, player, sections)?.toIntOrNull(),
            data.getString("transform")
        )
    }

    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param content 待重复内容
     * @param separator 分隔符
     * @param prefix 前缀
     * @param postfix 后缀
     * @param repeat 长度限制
     * @param transform 操作函数
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        content: String,
        separator: String?,
        prefix: String?,
        postfix: String?,
        repeat: Int?,
        transformString: String?,
    ): String {
        // 获取长度限制
        val length = (repeat ?: 1).coerceAtLeast(0)
        // 获取操作函数
        val transform = transformString?.let {
            compiledScripts.computeIfAbsent(it) {
                CompiledScript(
                    """
                    function main() {
                        $it
                    }""".trimIndent()
                )
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
            map["vars"] =
                java.util.function.Function<String, String> { string -> string.parseSection(cache, player, sections) }
        }

        for (index in 0 until length) {
            // 解析元素节点
            var element = content
            // 操作元素
            transform?.let {
                // 当前序号
                map["index"] = index
                // 操作元素
                element = transform.invoke("main", map)?.toString() ?: ""
            }
            // 添加元素
            result.append(element)
            // 添加分隔符
            separator?.let {
                if (index != (length - 1)) {
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
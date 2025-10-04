package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.OfflinePlayer
import java.util.function.BiFunction

/**
 * PlaceholderAPI挂钩
 */
abstract class PapiHooker {
    companion object {
        /**
         * 将文本中的所有papi变量改写为papi节点, 不管注没注册, 长得像papi节点就转换
         *
         * @param text 待转换文本
         * @return 转换后文本
         */
        @JvmStatic
        fun toAllSection(text: String): String {
            val chars = text.toCharArray()
            val builder = StringBuilder(text.length)

            val identifier = StringBuilder()
            val parameters = StringBuilder()

            var i = 0
            while (i < chars.size) {
                val l = chars[i]

                if ((l != '%') || ((i + 1) >= chars.size)) {
                    builder.append(l)
                    i++
                    continue
                }

                var identified = false
                var invalid = true
                var hadSpace = false

                while (++i < chars.size) {
                    val p = chars[i]

                    if (p == ' ' && !identified) {
                        hadSpace = true
                        break
                    }
                    if (p == '%') {
                        invalid = false
                        break
                    }

                    if (p == '_' && !identified) {
                        identified = true
                        continue
                    }

                    if (identified) {
                        parameters.append(p)
                    } else {
                        identifier.append(p)
                    }
                }

                val identifierString = identifier.toString()
                val parametersString = parameters.toString()

                identifier.setLength(0)
                parameters.setLength(0)

                if (invalid) {
                    builder.append('%').append(identifierString)

                    if (identified) {
                        builder.append('_').append(parametersString)
                    }

                    if (hadSpace) {
                        builder.append(' ')
                    }
                    i++
                    continue
                }

                val replacement = "<papi::${identifierString}_$parametersString>"

                builder.append(replacement)
                i++
            }

            return builder.toString()
        }
    }

    /**
     * 解析一段文本中的papi变量, 不解析其中的颜色代码
     * 在以往的众多版本中, papi都会强制解析文本中的代码
     * 在2.11.2版本中, papi移除了该功能
     * 不过, 管他呢, 自己实现就完事儿了
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    abstract fun papi(player: OfflinePlayer?, text: String): String

    /**
     * 将文本中的所有papi变量改写为papi节点, 仅转换已注册的papi变量
     *
     * @param text 待转换文本
     * @return 转换后文本
     */
    abstract fun toSection(text: String): String

    /**
     * 将文本中的所有papi变量改写为papi节点
     *
     * @param text 待转换文本
     * @param onlyValid 仅转换已注册的papi变量
     * @return 转换后文本
     */
    fun toSection(text: String, onlyValid: Boolean): String {
        return if (onlyValid) {
            toSection(text)
        } else {
            toAllSection(text)
        }
    }

    /**
     * 判断文本中是否存在有效papi变量
     *
     * @param text 待检测文本
     * @return 是否存在有效papi变量
     */
    abstract fun hasPapi(text: String): Boolean

    /**
     * 例：%player_name%
     * identifier -> player
     * parameters -> name
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param identifier PAPI标识符
     * @param parameters PAPI参数
     * @return 解析后文本
     */
    abstract fun request(player: OfflinePlayer?, identifier: String, parameters: String): String

    /**
     * 卸载papi扩展
     *
     * @param expansion papi扩展
     */
    abstract fun unregisterExpansion(expansion: PlaceholderExpansion)

    /**
     * 新建一个papi扩展
     *
     * @param identifier papi扩展名
     * @param author 扩展作者
     * @param version 扩展版本
     * @param executor 变量处理器
     * @return papi扩展
     */
    fun newPlaceholderExpansion(
        identifier: String, author: String, version: String, executor: BiFunction<OfflinePlayer?, String, String>
    ): PlaceholderExpansion {
        return PlaceholderExpansion(identifier, author, version, executor)
    }
}
package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.parseObject
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.section.Section
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagType
import java.awt.Color
import java.util.*

/**
 * 节点相关工具类
 */
object SectionUtils {
    /**
     * 对文本进行节点解析
     *
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSection(
        cache: HashMap<String, String>? = null,
        player: OfflinePlayer? = null,
        sections: ConfigurationSection? = null
    ): String {
        val stack = ArrayList<Int>()
        val stringBuilders = ArrayList<StringBuilder>()
        val result = StringBuilder()
        val chars = this.toCharArray()
        var backslash = false
        for (index in chars.indices) {
            val char = chars[index]
            if (char == '<' && !backslash) {
                // 压栈
                stack.add(index)
                stringBuilders.add(StringBuilder())
                // 如果是右括号
            } else if (char == '>' && !backslash) {
                // 前面有左括号了
                if (stack.isNotEmpty()) {
                    // 还不止一个
                    if (stack.size > 1) {
                        // 出栈
                        stack.removeLast()
                        val string = stringBuilders.removeLast().toString().getSection(cache, player, sections)
                        stringBuilders[stack.lastIndex].append(string)
                        // 只有一个
                    } else {
                        // 记录并出栈
                        stack.removeLast()
                        val string = stringBuilders.removeLast().toString().getSection(cache, player, sections)
                        result.append(string)
                    }
                } else {
                    result.append(char)
                }
            } else {
                if (stack.isNotEmpty()) {
                    if (char != '<' && char != '>' && backslash) {
                        stringBuilders[stack.lastIndex].append('\\')
                    }
                    if (char != '\\') {
                        stringBuilders[stack.lastIndex].append(char)
                    }
                } else {
                    if (char != '<' && char != '>' && backslash) {
                        result.append('\\')
                    }
                    if (char != '\\') {
                        result.append(char)
                    }
                }
            }
            if (char == '\\') {
                backslash = true
            } else {
                backslash = false
            }
        }
        if (stringBuilders.isNotEmpty()) {
            for (stringBuilder in stringBuilders) {
                result.append('<')
                result.append(stringBuilder)
            }
        }
        return result.toString()
    }

    /**
     * 加载字符串最外层<>位置(测试)
     *
     * @param string 待加载文本
     * @param start 用于存储<位置
     * @param end 用于存储>位置
     */
    @JvmStatic
    fun loadTest(string: String, start: ArrayList<Int>, end: ArrayList<Int>) {
        // 经测试, 这比LinkedList还略快一筹
        val stack = ArrayList<Int>()
        val chars = string.toCharArray()
        var backslash = false
        for (index in chars.indices) {
            val char = chars[index]
            if (char == '<' && !backslash) {
                // 压栈
                stack.add(index)
                // 如果是右括号
            } else if (char == '>' && !backslash) {
                // 前面有左括号了
                if (stack.isNotEmpty()) {
                    // 还不止一个
                    if (stack.size > 1) {
                        // 出栈
                        stack.removeLast()
                        // 只有一个
                    } else {
                        // 记录并出栈
                        start.add(stack.removeLast())
                        end.add(index)
                    }
                }
            }
            if (char == '\\') {
                backslash = true
            } else {
                backslash = false
            }
        }
    }

    /**
     * 对文本进行节点解析(弃用做法)
     *
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSectionOld(
        cache: HashMap<String, String>? = null,
        player: OfflinePlayer? = null,
        sections: ConfigurationSection? = null
    ): String {
        // 定位最外层 <> 包裹的字符串
        val start = ArrayList<Int>()
        val end = ArrayList<Int>()
        load(this, start, end)
        if (start.size == 0) return this
        // 对 <> 包裹的文本进行节点解析
        val listString = StringBuilder(this.substring(0, start[0]))
        for (index in 0 until start.size) {
            // 解析目标文本
            listString.append(
                // 先截取文本
                this.substring(start[index]+1, end[index])
                    // 因为取的是最外层 <> 包裹的字符串, 所以内部可能还需要继续解析
                    .parseSection(cache, player, sections)
                    // 解析完成后可以视作节点ID/即时声明节点, 进行节点调用
                    .getSection(cache, player, sections)
            )

            if (index+1 != start.size) {
                listString.append(this.substring(end[index]+1, start[(start.size-1).coerceAtMost(index+1)]))
            } else {
                listString.append(this.substring(end[index]+1, this.length))
            }
        }
        return listString.toString()
    }

    /**
     * 对文本进行节点解析
     *
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSection(): String {
        return this.parseSection(null, null, null)
    }

    /**
     * 对文本进行节点解析
     *
     * @param cache 解析值缓存
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSection(cache: HashMap<String, String>?): String {
        return this.parseSection(cache, null, null)
    }

    /**
     * 对文本进行节点解析
     *
     * @param player 待解析玩家
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSection(player: OfflinePlayer?): String {
        return this.parseSection(null, player, null)
    }

    /**
     * 对文本进行节点解析
     *
     * @param cache 解析值缓存
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSection(cache: HashMap<String, String>?, sections: ConfigurationSection?): String {
        return this.parseSection(cache, null, sections)
    }

    /**
     * 对文本进行节点解析
     *
     * @param parse 是否对文本进行节点解析
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.parseSection(
        parse: Boolean,
        cache: HashMap<String, String>? = null,
        player: OfflinePlayer? = null,
        sections: ConfigurationSection? = null
    ): String {
        return when {
            parse -> this.parseSection(cache, player, sections)
            else -> this
        }
    }

    /**
     * 对节点内容进行解析 (已经去掉 <>)
     *
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.getSection(
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        when (val index = this.indexOf("::")) {
            // 私有节点调用
            -1 -> {
                // 尝试读取缓存
                if (cache?.get(this) != null) {
                    // 直接返回对应节点值
                    return cache[this] as String
                // 读取失败, 尝试主动解析
                } else {
                    // 尝试解析并返回对应节点值
                    if (sections != null && sections.contains(this)) {
                        // 获取节点ConfigurationSection
                        val section = sections.getConfigurationSection(this)
                        // 简单节点
                        if (section == null) {
                            val result = sections.getString(this)?.parseSection(cache, player, sections) ?: "<$this>"
                            cache?.put(this, result)
                            return result
                        }
                        // 加载节点
                        return Section(section, this).load(cache, player, sections) ?: "<$this>"
                    }
                    if (this.startsWith("#")) {
                        try {
                            try {
                                val hex = (this.substring(1).toIntOrNull(16) ?: 0)
                                    .coerceAtLeast(0)
                                    .coerceAtMost(0xFFFFFF)
                                val color = Color(hex)
                                return ChatColor.of(color).toString()
                            } catch (error: NumberFormatException) {}
                        } catch (error: NoSuchMethodError) {
                            Bukkit.getLogger().info("§e[NI] §6低于1.16的版本不能使用16进制颜色哦")
                        }
                    }
                }
                return "<$this>"
            }
            // 即时声明节点解析
            else -> {
                // 获取节点类型
                val type = this.substring(0, index)
                // 获取参数
                val args = this.substring(index+2).split("(?<!\\\\)_".toRegex()).map { it.replace("\\_", "_") }
                return SectionManager.sectionParsers[type]?.onRequest(args, cache, player, sections) ?: "<$this>"
            }
        }
    }

    /**
     * 对文本进行物品节点解析
     *
     * @param itemTag 物品NBT
     * @return 解析值
     */
    @JvmStatic
    fun String.parseItemSection(itemTag: ItemTag, player: OfflinePlayer): String {
        // 定位最外层 <> 包裹的字符串
        val start = ArrayList<Int>()
        val end = ArrayList<Int>()
        load(this, start, end)
        if (start.size == 0) return this
        // 对 <> 包裹的文本进行节点解析
        val listString = StringBuilder(this.substring(0, start[0]))
        for (index in 0 until start.size) {
            // 解析目标文本
            listString.append(
                this.substring(start[index]+1, end[index])
                    .parseItemSection(itemTag, player)
                    .getItemSection(itemTag, player)
            )

            if (index+1 != start.size) {
                listString.append(this.substring(end[index]+1, start[(start.size-1).coerceAtMost(index+1)]))
            } else {
                listString.append(this.substring(end[index]+1, this.length))
            }
        }
        return listString.toString()
    }

    /**
     * 对物品节点内容进行解析 (已经去掉 <>)
     *
     * @param itemTag 物品NBT
     * @return 解析值
     */
    @JvmStatic
    fun String.getItemSection(itemTag: ItemTag, player: OfflinePlayer): String {
        val string = this
            .replace("\\<", "<")
            .replace("\\>", ">")
        when (val index = string.indexOf("::")) {
            -1 -> {
                return "<$string>"
            }
            else -> {
                val name = string.substring(0, index)
                val args = string.substring(index + 2)
                return when (name.lowercase(Locale.getDefault())) {
                    "nbt" -> {
                        var value: ItemTagData = itemTag
                        val argsArray: Array<String> = args.split(".").toTypedArray()

                        argsArray.forEach { key ->
                            when (value.type) {
                                ItemTagType.LIST -> {
                                    key.toIntOrNull()?.let { index ->
                                        val list = value.asList()
                                        if (list.size > index) {
                                            value.asList()[index.coerceAtLeast(0)].also { value = it } ?: let { value = ItemTagData("<$string>") }
                                        } else { value = ItemTagData("<$string>") }
                                    } ?: let { value = ItemTagData("<$string>") }
                                }
                                ItemTagType.COMPOUND -> value.asCompound()[key]?.also { value = it } ?: let { value = ItemTagData("<$string>") }
                                else -> let { value = ItemTagData("<$string>") }
                            }
                        }

                        return value.asString()
                    }
                    "data" -> {
                        val data = itemTag["NeigeItems"]?.asCompound()?.get("data")?.asString()
                        data?.parseObject<HashMap<String, String>>()?.get(args) ?: "<$string>"
                    }
                    else -> {
                        return SectionManager.sectionParsers[name]?.onRequest(args.split("_"), null, player) ?: "<$string>"
                    }
                }
            }
        }
    }

    /**
     * 加载字符串最外层<>位置
     *
     * @param string 待加载文本
     * @param start 用于存储<位置
     * @param end 用于存储>位置
     */
    @JvmStatic
    fun load(string: String, start: ArrayList<Int>, end: ArrayList<Int>) {
        // 经测试, 这比LinkedList还略快一筹
        val stack = ArrayList<Int>()
        val chars = string.toCharArray()
        var backslash = false
        for (index in chars.indices) {
            val char = chars[index]
            if (char == '<' && !backslash) {
                // 压栈
                stack.add(index)
                // 如果是右括号
            } else if (char == '>' && !backslash) {
                // 前面有左括号了
                if (stack.isNotEmpty()) {
                    // 还不止一个
                    if (stack.size > 1) {
                        // 出栈
                        stack.removeLast()
                        // 只有一个
                    } else {
                        // 记录并出栈
                        start.add(stack.removeLast())
                        end.add(index)
                    }
                }
            }
            if (char == '\\') {
                backslash = true
            } else {
                backslash = false
            }
        }
    }
}
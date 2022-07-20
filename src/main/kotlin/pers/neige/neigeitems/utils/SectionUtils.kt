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

object SectionUtils {
    /**
     * 对文本进行节点解析
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
                    .parseSection(cache, player, sections)
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

    @JvmStatic
    fun String.parseSection(player: OfflinePlayer?): String {
        return this.parseSection(null, player, null)
    }

    @JvmStatic
    fun String.parseSection(cache: HashMap<String, String>? = null, sections: ConfigurationSection? = null): String {
        return this.parseSection(cache, null, sections)
    }

    /**
     * 对节点内容进行解析 (已经去掉 <>)
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
                        return Section(section).load(cache, player, sections) ?: "<$this>"
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
                val args = this.substring(index+2).split("_")
                return SectionManager.sectionParsers[type]?.onRequest(args, cache, player, sections) ?: "<$this>"
            }
        }
    }

    /**
     * 对文本进行物品节点解析
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
     * @param itemTag 物品NBT
     * @return 解析值
     */
    @JvmStatic
    fun String.getItemSection(itemTag: ItemTag, player: OfflinePlayer): String {
        when (val index = this.indexOf("::")) {
            -1 -> {
                return "<$this>"
            }
            else -> {
                val name = this.substring(0, index)
                val args = this.substring(index + 2)
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
                                            value.asList()[index.coerceAtLeast(0)].also { value = it } ?: let { value = ItemTagData("<$this>") }
                                        } else { value = ItemTagData("<$this>") }
                                    } ?: let { value = ItemTagData("<$this>") }
                                }
                                ItemTagType.COMPOUND -> value.asCompound()[key]?.also { value = it } ?: let { value = ItemTagData("<$this>") }
                                else -> let { value = ItemTagData("<$this>") }
                            }
                        }

                        return value.asString()
                    }
                    "data" -> {
                        val data = itemTag["NeigeItems"]?.asCompound()?.get("data")?.asString()
                        data?.parseObject<HashMap<String, String>>()?.get(args) ?: "<$this>"
                    }
                    else -> {
                        return SectionManager.sectionParsers[name]?.onRequest(args.split("_"), null, player) ?: "<$this>"
                    }
                }
            }
        }
    }

    // 加载字符串最外层<>位置
    private fun load(string: String, start: ArrayList<Int>, end: ArrayList<Int>) {
        val stack = LinkedList<Int>()
        string.forEachIndexed { index, char ->
            // 如果是待识别的左括号
            if (char == '<' && (string[0.coerceAtLeast(index - 1)] != '\\')) {
                // 压栈
                stack.push(index)
                // 如果是右括号
            } else if (char == '>' && string[(string.length-1).coerceAtMost(index + 1)] != '\\') {
                // 前面有左括号了
                if (!stack.isEmpty()) {
                    // 还不止一个
                    if (stack.size > 1) {
                        // 出栈
                        stack.pop()
                        // 只有一个
                    } else {
                        // 记录并出栈
                        start.add(stack.poll())
                        end.add(index)
                    }
                }
            }}
    }
}
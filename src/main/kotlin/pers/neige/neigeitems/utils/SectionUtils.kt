package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.parseObject
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.section.Section
import pers.neige.neigeitems.utils.ItemUtils.getDamage
import pers.neige.neigeitems.utils.SectionUtils.getSection
import pers.neige.neigeitems.utils.StringUtils.split
import java.awt.Color
import java.util.*
import java.util.function.Function

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
        cache: MutableMap<String, String>? = null, player: OfflinePlayer? = null, sections: ConfigurationSection? = null
    ): String {
        return this.parse {
            return@parse it.getSection(cache, player, sections)
        }
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
    fun String.parseSection(cache: MutableMap<String, String>?): String {
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
    fun String.parseSection(cache: MutableMap<String, String>?, sections: ConfigurationSection?): String {
        return this.parseSection(cache, null, sections)
    }

    /**
     * 对文本进行节点解析
     * parse参数为false时不对文本进行解析
     * 用意为不解析即时声明节点的参数(处理即时节点的SectionParser.onRequest时将parse定义为false, 解析参数时传入parse即可)
     * 因为即时声明节点能传入进来一定是经过了parseSection
     * 而这一步会对文本进行全局节点解析
     * 即: 参数为解析后分割传入的
     * 对于已解析的参数, 多解析一次等于浪费时间
     * 注: 但inherit节点不能盲目调用parse, 因为继承而来的文本一定是未解析的
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
        cache: MutableMap<String, String>? = null,
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
        cache: MutableMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?
    ): String {
        val index = this.indexOf("::")
        // 私有节点调用
        if (index == -1) {
            // 尝试读取缓存
            if (cache?.containsKey(this) == true) {
                // 直接返回对应节点值
                return cache[this].toString()
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
                    val rgb = this.substring(1).toIntOrNull(16)
                    if (rgb != null) {
                        return ColorUtils.toHexColorPrefix(rgb)
                    }
                }
            }
            return "<$this>"
            // 即时声明节点解析
        } else {
            // 获取节点类型
            val type = this.substring(0, index)
            // 所有参数
            val args = this.substring(index + 2).split('_', '\\')
            return SectionManager.sectionParsers[type]?.onRequest(args, cache, player, sections) ?: "<$this>"
        }
    }

    /**
     * 对文本进行物品节点解析
     *
     * @param itemStack 物品
     * @param itemTag 物品NBT
     * @param player 用于解析物品的玩家
     * @return 解析值
     */
    @JvmStatic
    fun String.parseItemSection(
        itemStack: ItemStack, itemTag: NbtCompound, player: OfflinePlayer?
    ): String {
        return parseItemSection(itemStack, itemTag, null, player, null, null)
    }

    /**
     * 对文本进行物品节点解析
     *
     * @param itemStack 物品
     * @param itemTag 物品NBT
     * @param data NeigeItems.data 代表NI物品节点数据
     * @param player 用于解析物品的玩家
     * @return 解析值
     */
    @JvmStatic
    @Deprecated("多数情况下, 调用此方法意味着可能出现无意义性能损失")
    fun String.parseItemSection(
        itemStack: ItemStack, itemTag: NbtCompound, data: MutableMap<String, String>?, player: OfflinePlayer?
    ): String {
        return parseItemSection(itemStack, itemTag, data, player, null, null)
    }

    /**
     * 对文本进行物品节点解析
     *
     * @param itemStack 物品
     * @param itemInfo 物品信息
     * @param player 用于解析物品的玩家
     * @return 解析值
     */
    @JvmStatic
    fun String.parseItemSection(
        itemStack: ItemStack, itemInfo: ItemInfo, player: OfflinePlayer?
    ): String {
        return parseItemSection(itemStack, itemInfo, player, null, null)
    }

    /**
     * 对文本进行物品节点解析
     *
     * @param itemStack 物品
     * @param itemInfo 物品信息
     * @param player 用于解析物品的玩家
     * @param cache 解析值缓存
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.parseItemSection(
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        player: OfflinePlayer?,
        cache: MutableMap<String, String>?,
        sections: ConfigurationSection?
    ): String {
        return this.parse {
            return@parse it.getItemSection(itemStack, itemInfo, player, cache, sections)
        }
    }

    /**
     * 对文本进行物品节点解析
     *
     * @param itemStack 物品
     * @param itemTag 物品NBT
     * @param data NeigeItems.data 代表NI物品节点数据
     * @param player 用于解析物品的玩家
     * @param cache 解析值缓存
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    @Deprecated("多数情况下, 调用此方法意味着可能出现无意义性能损失")
    fun String.parseItemSection(
        itemStack: ItemStack,
        itemTag: NbtCompound,
        data: MutableMap<String, String>?,
        player: OfflinePlayer?,
        cache: MutableMap<String, String>?,
        sections: ConfigurationSection?
    ): String {
        return this.parse {
            return@parse it.getItemSection(itemStack, itemTag, data, player, cache, sections)
        }
    }

    /**
     * 对物品节点内容进行解析 (已经去掉 <>)
     *
     * @param itemStack 物品
     * @param itemTag 物品NBT
     * @param data NeigeItems.data 代表NI物品节点数据
     * @param player 用于解析物品的玩家
     * @param cache 解析值缓存
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    @Deprecated("多数情况下, 调用此方法意味着可能出现无意义性能损失")
    fun String.getItemSection(
        itemStack: ItemStack,
        itemTag: NbtCompound,
        data: MutableMap<String, String>?,
        player: OfflinePlayer?,
        cache: MutableMap<String, String>? = null,
        sections: ConfigurationSection? = null
    ): String {
        val index = this.indexOf("::")
        if (index == -1) {
            // 尝试读取缓存
            if (cache?.containsKey(this) == true) {
                // 直接返回对应节点值
                return cache[this].toString()
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
                    val rgb = this.substring(1).toIntOrNull(16)
                    if (rgb != null) {
                        return ColorUtils.toHexColorPrefix(rgb)
                    }
                }
            }
            return "<$this>"
        } else {
            // 获取节点类型
            val name = this.substring(0, index)
            val param = this.substring(index + 2)
            return when (name.lowercase(Locale.getDefault())) {
                "nbt" -> {
                    itemTag.getDeepStringOrNull(param) ?: "<$this>"
                }

                "data" -> {
                    (data ?: itemTag.getStringOrNull("NeigeItems.data")?.parseObject<HashMap<String, String>>())?.get(
                        param
                    ) ?: "<$this>"
                }

                "amount" -> {
                    itemStack.amount.toString()
                }

                "type" -> {
                    itemStack.type.toString()
                }

                "name" -> {
                    TranslationUtils.getDisplayOrTranslationName(itemStack)
                }

                "damage" -> {
                    itemStack.getDamage().toString()
                }

                else -> {
                    SectionManager.sectionParsers[name]?.onRequest(param.split('_', '\\'), null, player) ?: "<$this>"
                }
            }
        }
    }

    /**
     * 对物品节点内容进行解析 (已经去掉 <>)
     *
     * @param itemStack 物品
     * @param itemInfo 物品信息
     * @param player 用于解析物品的玩家
     * @param cache 解析值缓存
     * @param sections 节点池
     * @return 解析值
     */
    @JvmStatic
    fun String.getItemSection(
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        player: OfflinePlayer?,
        cache: MutableMap<String, String>? = null,
        sections: ConfigurationSection? = null
    ): String {
        val index = this.indexOf("::")
        if (index == -1) {
            // 尝试读取缓存
            if (cache?.containsKey(this) == true) {
                // 直接返回对应节点值
                return cache[this].toString()
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
                    val rgb = this.substring(1).toIntOrNull(16)
                    if (rgb != null) {
                        return ColorUtils.toHexColorPrefix(rgb)
                    }
                }
            }
            return "<$this>"
        } else {
            // 获取节点类型
            val name = this.substring(0, index)
            val param = this.substring(index + 2)
            return when (name.lowercase(Locale.getDefault())) {
                "nbt" -> {
                    itemInfo.itemTag.getDeepStringOrNull(param) ?: "<$this>"
                }

                "data" -> {
                    itemInfo.data[param] ?: "<$this>"
                }

                "amount" -> {
                    itemStack.amount.toString()
                }

                "type" -> {
                    itemStack.type.toString()
                }

                "name" -> {
                    TranslationUtils.getDisplayOrTranslationName(itemStack)
                }

                "damage" -> {
                    itemStack.getDamage().toString()
                }

                else -> {
                    SectionManager.sectionParsers[name]?.onRequest(param.split('_', '\\'), null, player) ?: "<$this>"
                }
            }
        }
    }

    /**
     * 对文本进行某种解析
     *
     * @param params 参数
     * @return 解析值
     */
    @JvmStatic
    fun String.parse(params: Map<String, Any?>): String {
        return this.parse('<', '>', '\\') {
            params[it]?.toString()
        }
    }

    /**
     * 对文本进行某种解析
     *
     * @param transform 解析函数
     * @return 解析值
     */
    @JvmStatic
    fun String.parse(transform: Function<String, String?>): String {
        return this.parse('<', '>', '\\', transform)
    }

    /**
     * 对文本进行某种解析
     *
     * @param head 节点起始标识
     * @param tail 节点终止标识
     * @param escape 转义符
     * @param transform 解析函数
     * @return 解析值
     */
    @JvmStatic
    fun String.parse(head: Char, tail: Char, escape: Char, transform: Function<String, String?>): String {
        // 缓存待解析节点
        val stringBuilders = ArrayList<StringBuilder>()
        // 储存解析结果
        val result = StringBuilder()
        val chars = this.toCharArray()
        // 表示前一个字符是不是转义符
        var lastIsEscape = false
        // 遍历
        for (char in chars) {
            // 当前字符是不是起始标识
            val isHead = char == head
            // 当前字符是不是终止标识
            val isTail = char == tail
            // 当前字符是不是转义符
            val isEscape = char == escape
            // 当前为节点起始标识且前一字符不是转义符, 代表节点的起始
            if (isHead && !lastIsEscape) {
                // 添加一个新的StringBuilder, 用于缓存当前节点文本
                stringBuilders.add(StringBuilder())
                // 当前为节点终止标识且前一字符不是转义符
            } else if (isTail && !lastIsEscape) {
                // 前面有节点起始标识
                if (stringBuilders.isNotEmpty()) {
                    // 还不止一个
                    if (stringBuilders.size > 1) {
                        // 证明这是一个嵌套节点, 解析当前内容, append进父节点的待解析文本里, 移除当前节点的缓存
                        val string = stringBuilders.removeAt(stringBuilders.lastIndex).toString()
                        stringBuilders[stringBuilders.lastIndex].append(transform.apply(string) ?: "$head$string$tail")
                        // 只有一个
                    } else {
                        // 证明当前节点是一个顶级节点, 解析当前内容, append进结果文本, 移除节点缓存
                        val string = stringBuilders.removeAt(stringBuilders.lastIndex).toString()
                        result.append(transform.apply(string) ?: "$head$string$tail")
                    }
                } else {
                    // 因为已经检查过, 当前字符不被转义, 且不为反斜杠, 所以可以直接append
                    result.append(char)
                }
                // 字符不为节点的起始或终止
            } else {
                // 如果stringBuilders不为空, 说明当前仍在解析进行节点识别, 应将字符填入节点缓存
                // 如果stringBuilders为空, 说明当前处于顶层, 应直接填入result
                val stringBuilder = when {
                    stringBuilders.isNotEmpty() -> stringBuilders[stringBuilders.lastIndex]
                    else -> result
                }
                // 如果前一字符为转义符, 理应判断当前字符是否需要转义
                // 需要转义则吞掉转义符, 不需转义则将转义符视作普通反斜杠处理
                if (!isHead && !isTail && !isEscape && lastIsEscape) {
                    stringBuilder.append(escape)
                }
                // 如果当前字符不为转义符, 直接填入字符
                if (!isEscape || lastIsEscape) {
                    stringBuilder.append(char)
                }
            }
            // 进行转义符记录
            lastIsEscape = isEscape && !lastIsEscape
        }
        // 遍历结束后stringBuilders不为空, 说明文本中存在冗余节点起始标识
        // 对于此种情况, 应将该标识符视作普通文本处理
        if (stringBuilders.isNotEmpty()) {
            // 遍历stringBuilders
            for (stringBuilder in stringBuilders) {
                // 填入节点起始标识
                result.append(head)
                // 填入文本
                result.append(stringBuilder)
            }
        }
        return result.toString()
    }
}
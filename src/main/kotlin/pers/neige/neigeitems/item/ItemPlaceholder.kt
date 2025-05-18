package pers.neige.neigeitems.item

import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.event.ItemPacketEvent
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtNumeric
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtString
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull
import pers.neige.neigeitems.utils.ListenerUtils
import java.util.*
import java.util.function.BiFunction

/**
 * 用于实现物品变量功能
 */
object ItemPlaceholder {
    /**
     * 1.20.5+ 版本起, Mojang献祭了自己的亲妈, 换来了物品格式的改动.
     */
    @JvmStatic
    private val MOJANG_MOTHER_DEAD = CbVersion.v1_20_R4.isSupport

    /**
     * 获取物品变量附属
     */
    val expansions = HashMap<String, BiFunction<ItemStack, String, String?>>()

    /**
     * 用于添加物品变量附属
     *
     * @param id 变量ID
     * @param function 操作函数
     */
    fun addExpansion(id: String, function: BiFunction<ItemStack, String, String?>) {
        expansions[id.lowercase(Locale.getDefault())] = function
    }

    /**
     * 解析物品名和物品Lore中的物品变量
     *
     * @param itemStack 待解析物品
     */
    fun itemParse(itemStack: ItemStack) {
        if (NbtUtils.isCraftItemStack(itemStack)) {
            if (MOJANG_MOTHER_DEAD) {
                NbtUtils.editNameAndLoreAfterV21(itemStack, this::parse)
            } else {
                val nbt = itemStack.getNbtOrNull() ?: return
                val display = nbt.getCompound("display") ?: return
                display.getString("Name")?.let { name ->
                    val parsed = parse(itemStack, name)
                    if (parsed.changed) {
                        display.putString("Name", parsed.text)
                    }
                }
                display.getList("Lore")?.let { lore ->
                    lore.forEachIndexed { index, nbt ->
                        val parsed = parse(itemStack, nbt.asString)
                        if (parsed.changed) {
                            lore[index] = NbtString.valueOf(parsed.text)
                        }
                    }
                }
            }
        }
    }

    init {
        // 加载基础变量
        addExpansion("neigeitems") { itemStack, param ->
            val args = param.split("_", limit = 2)
            val itemTag = itemStack.getNbtOrNull() ?: return@addExpansion null
            when (args[0].lowercase(Locale.getDefault())) {
                "charge" -> {
                    itemTag.getDeepIntOrNull("NeigeItems.charge")?.toString()
                }

                "maxcharge" -> {
                    itemTag.getDeepIntOrNull("NeigeItems.maxCharge")?.toString()
                }

                "durability" -> {
                    itemTag.getDeepIntOrNull("NeigeItems.durability")?.toString()
                }

                "maxdurability" -> {
                    itemTag.getDeepIntOrNull("NeigeItems.maxDurability")?.toString()
                }

                "itembreak" -> {
                    val info = args.getOrNull(1)?.split("_", limit = 2)
                    val itemBreak = itemTag.getDeepBoolean("NeigeItems.itemBreak", true)
                    // 值为1或不存在(这种情况itemBreak是true)代表损坏
                    if (itemBreak) {
                        info?.getOrNull(1)
                        // 值为0代表不损坏
                    } else {
                        info?.getOrNull(0)
                    }
                }

                "nbt" -> {
                    getDeepStringOrNull(itemTag, args.getOrNull(1) ?: "")
                }

                "nbtnumber" -> {
                    val info = args.getOrNull(1)?.split("_", limit = 2)
                    val value = getDeepDoubleOrNull(itemTag, info?.getOrNull(1) ?: "") ?: return@addExpansion null
                    "%.${info?.getOrNull(0)}f".format(value)

                }

                else -> null
            }
        }
    }

    @Awake
    private fun initListener() {
        // 检测是否开启物品变量功能
        if (config.getBoolean("ItemPlaceholder.enable")) {
            ListenerUtils.registerListener(ItemPacketEvent::class.java) {
                itemParse(it.itemStack)
            }
        }
    }

    private fun getDeepDoubleOrNull(nbt: NbtComponentLike, key: String): Double? {
        val value: Nbt<*>? = nbt.getDeep(key, '`', '\\')
        return if (value is NbtNumeric<*>) {
            value.asDouble
        } else null
    }

    private fun getDeepStringOrNull(nbt: NbtComponentLike, key: String): String? {
        val value: Nbt<*>? = nbt.getDeep(key, '`', '\\')
        return if ((value != null)) {
            value.asString
        } else null
    }

    class ParseResult(val text: String, val changed: Boolean)

    /**
     * 根据物品解析物品变量
     *
     * @param itemStack 用于解析变量的物品
     * @param text 待解析文本
     * @return 解析后文本
     */
    fun parse(itemStack: ItemStack, text: String): ParseResult {
        val chars = text.toCharArray()
        val builder = StringBuilder(text.length)

        val identifier = StringBuilder()
        val parameters = StringBuilder()
        var changed = false

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
            val lowercaseIdentifierString = identifierString.lowercase(Locale.getDefault())
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

            val placeholder = expansions[lowercaseIdentifierString.replace(Regex("""§+[a-z0-9]"""), "")]
            if (placeholder == null) {
                builder.append('%').append(identifierString)

                if (identified) {
                    builder.append('_')
                }

                builder.append(parametersString).append('%')
                i++
                continue
            }

            val replacement = placeholder.apply(itemStack, parametersString)
            if (replacement == null) {
                builder.append('%').append(identifierString)

                if (identified) {
                    builder.append('_')
                }

                builder.append(parametersString).append('%')
                i++
                continue
            }

            builder.append(replacement)
            changed = true
            i++
        }

        return ParseResult((if (changed) builder.toString() else text), changed)
    }
}
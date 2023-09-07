package pers.neige.neigeitems.item

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.GameMode
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.ItemUtils.getDeepOrNull
import taboolib.module.nms.getItemTag
import java.util.*
import java.util.function.BiFunction

/**
 * 用于实现物品变量功能
 */
class ItemPlaceholder {
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
        itemStack.itemMeta?.let { itemMeta ->
            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(parse(itemStack, itemMeta.displayName))
            }
            if (itemMeta.hasLore()) {
                val lore = itemMeta.lore
                if (lore != null) {
                    for (index in lore.indices) {
                        lore[index] = parse(itemStack, lore[index])
                    }
                }
                itemMeta.lore = lore
            }
            itemStack.setItemMeta(itemMeta)
        }
    }

    init {
        // 加载基础变量
        addExpansion("neigeitems") { itemStack, param ->
            val args = param.split("_", limit = 2)
            val itemTag = itemStack.getItemTag()
            when (args[0].lowercase(Locale.getDefault())) {
                "charge" -> {
                    itemTag.getDeepOrNull("NeigeItems.charge")?.asString()
                }
                "maxcharge" -> {
                    itemTag.getDeepOrNull("NeigeItems.maxCharge")?.asString()
                }
                "durability" -> {
                    itemTag.getDeepOrNull("NeigeItems.durability")?.asString()
                }
                "maxdurability" -> {
                    itemTag.getDeepOrNull("NeigeItems.maxDurability")?.asString()
                }
                "itembreak" -> {
                    val info = args.getOrNull(1)?.split("_", limit = 2)
                    val itemBreak = itemTag.getDeepOrNull("NeigeItems.itemBreak")?.asByte()
                    // 值为0代表不损坏
                    if (itemBreak == 0.toByte()) {
                        info?.getOrNull(0)
                    // 值为1或不存在代表损坏
                    } else {
                        info?.getOrNull(1)
                    }
                }
                "nbt" -> {
                    itemTag.getDeepOrNull(args.getOrNull(1) ?: "")?.asString()
                }
                "nbtnumber" -> {
                    val info = args.getOrNull(1)?.split("_", limit = 2)
                    "%.${info?.getOrNull(0)}f".format(itemTag.getDeepOrNull(info?.getOrNull(1) ?: "")?.asDouble())
                }
                else -> null
            }
        }

        // 检测是否开启物品变量功能
        if (config.getBoolean("ItemPlaceholder.enable")) {
            // 监听数据包进行变量替换
            ProtocolLibrary.getProtocolManager().addPacketListener(object :
                PacketAdapter(
                    plugin,
                    ListenerPriority.NORMAL,
                    PacketType.Play.Server.WINDOW_ITEMS,
                    PacketType.Play.Server.SET_SLOT) {
                override fun onPacketSending(event: PacketEvent) {
                    val gameMode = event.player.gameMode
                    if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
                        if (event.packetType == PacketType.Play.Server.WINDOW_ITEMS) {
                            val items = event.packet.itemListModifier.read(0)
                            items.forEach { itemStack ->
                                itemParse(itemStack)
                            }
                            event.packet.itemListModifier.write(0, items)
                        } else {
                            val itemStack = event.packet.itemModifier.read(0)
                            itemParse(itemStack)
                            event.packet.itemModifier.write(0, itemStack)
                        }
                    }
                }
                override fun onPacketReceiving(event: PacketEvent) {}
            }
            )
        }
    }

    /**
     * 根据物品解析物品变量
     *
     * @param itemStack 用于解析变量的物品
     * @param text 待解析文本
     * @return 解析后文本
     */
    fun parse(itemStack: ItemStack, text: String): String {
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
            i++
        }

        return builder.toString()
    }
}
package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.positiveInteger
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.HookerManager.append
import pers.neige.neigeitems.manager.HookerManager.getParsedComponent
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.manager.HookerManager.hoverItem
import pers.neige.neigeitems.manager.HookerManager.hoverText
import pers.neige.neigeitems.manager.HookerManager.runCommand
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.PlayerUtils.sendMessage
import pers.neige.neigeitems.utils.SchedulerUtils.async
import kotlin.math.ceil

/**
 * ni list指令
 */
object List {
    val list: LiteralArgumentBuilder<CommandSender> =
        // ni list
        literal<CommandSender>("list").executes { context ->
            list(context.source)
            1
        }.then(
            // ni list (page)
            argument<CommandSender, Int>("page", positiveInteger()).executes { context ->
                list(context.source, getInteger(context, "page"))
                1
            }.suggests { _, builder ->
                for (i in 1..ceil(ItemManager.itemAmount.toDouble() / ConfigManager.config.getDouble("ItemList.ItemAmount")).toInt()) {
                    builder.suggest(i.toString())
                }
                builder.buildFuture()
            }
        )

    private fun list(
        sender: CommandSender,
        page: Int = 1
    ) {
        async {
            listCommand(sender, page)
        }
    }

    private fun listCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 页码
        page: Int
    ) {
        val pageAmount =
            ceil(ItemManager.itemAmount.toDouble() / ConfigManager.config.getDouble("ItemList.ItemAmount")).toInt()
        val realPage = page.coerceAtMost(pageAmount).coerceAtLeast(1)
        // 发送前缀
        ConfigManager.config.getString("ItemList.Prefix")?.let { sender.sendMessage(it) }
        // 预构建待发送信息
        val listMessage = ComponentBuilder("")
        // 获取当前序号
        val prevItemAmount = ((realPage - 1) * ConfigManager.config.getInt("ItemList.ItemAmount")) + 1
        // 逐个获取物品
        for (index in (prevItemAmount until prevItemAmount + ConfigManager.config.getInt("ItemList.ItemAmount"))) {
            if (index == ItemManager.itemIds.size + 1) break
            val id = ItemManager.itemIds[index - 1]
            // 替换信息内变量
            var listItemMessage = (ConfigManager.config.getString("ItemList.ItemFormat") ?: "")
                .replace("{index}", index.toString())
                .replace("{ID}", id)
            // 构建信息及物品
            if (sender is Player) {
                kotlin.runCatching { ItemManager.getItemStack(id, sender) }.getOrNull()?.let { itemStack ->
                    val listItemMessageList = listItemMessage.split("{name}")
                    val listItemRaw = ComponentBuilder("")
                    for ((i, it) in listItemMessageList.withIndex()) {
                        listItemRaw.append(
                            ComponentBuilder(it)
                                .runCommand("/ni get $id")
                                .hoverText(ConfigManager.config.getString("Messages.clickGiveMessage") ?: "")
                        )
                        if (i + 1 != listItemMessageList.size) {
                            HookerManager.parseItemPlaceholders(itemStack)
                            listItemRaw.append(
                                ComponentBuilder(itemStack.getParsedComponent())
                                    .hoverItem(itemStack)
                                    .runCommand("/ni get $id")
                            )
                        }
                    }
                    sender.sendMessage(listItemRaw)
                }
            } else {
                // 在不传入玩家变量的情况下尝试构建物品获取物品名
                // 如果用户在js节点中调用了玩家对象, 这样搞就会报错
                kotlin.runCatching { ItemManager.getItemStack(id) }.getOrNull()?.let { itemStack ->
                    sender.sendMessage(listItemMessage.replace("{name}", itemStack.getParsedName()))
                } ?: let {
                    val itemKeySection = ItemManager.getOriginConfig(id)
                    val itemName = when {
                        itemKeySection?.contains("name") == true -> itemKeySection.getString("name")
                        else -> HookerManager.getMaterial(itemKeySection?.getString("material"))
                            ?.let { ItemStack(it).getName() }
                    }
                    listItemMessage = itemName?.let { listItemMessage.replace("{name}", it) }.toString()
                    sender.sendMessage(listItemMessage)
                }
            }
        }
        val prevRaw = ComponentBuilder(ConfigManager.config.getString("ItemList.Prev") ?: "")
        if (realPage != 1) {
            prevRaw
                .hoverText((ConfigManager.config.getString("ItemList.Prev") ?: "") + ": " + (realPage - 1).toString())
                .runCommand("/ni list ${realPage - 1}")
        }
        val nextRaw = ComponentBuilder(ConfigManager.config.getString("ItemList.Next") ?: "")
        if (realPage != pageAmount) {
            nextRaw.hoverText((ConfigManager.config.getString("ItemList.Next") ?: "") + ": " + (realPage + 1))
            nextRaw.runCommand("/ni list ${realPage + 1}")
        }
        var listSuffixMessage = (ConfigManager.config.getString("ItemList.Suffix") ?: "")
            .replace("{current}", realPage.toString())
            .replace("{total}", pageAmount.toString())
        if (sender is Player) {
            listSuffixMessage = listSuffixMessage
                .replace("{prev}", "!@#$%{prev}!@#$%")
                .replace("{next}", "!@#$%{next}!@#$%")
            val listSuffixMessageList = listSuffixMessage.split("!@#$%")
            listSuffixMessageList.forEach { value ->
                when (value) {
                    "{prev}" -> listMessage.append(prevRaw)
                    "{next}" -> listMessage.append(nextRaw)
                    else -> listMessage.append(value)
                }
            }
            // 向玩家发送信息
            sender.sendMessage(listMessage)
        } else {
            sender.sendMessage(
                listSuffixMessage
                    .replace("{prev}", ConfigManager.config.getString("ItemList.Prev") ?: "")
                    .replace("{next}", ConfigManager.config.getString("ItemList.Next") ?: "")
            )
        }
    }
}
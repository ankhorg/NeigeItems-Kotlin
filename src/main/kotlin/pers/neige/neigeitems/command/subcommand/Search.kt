package pers.neige.neigeitems.command.subcommand

import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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
import taboolib.common.platform.command.subCommand
import java.util.*
import kotlin.math.ceil

object Search {
    // ni search [ID前缀] (页码) > 查看对应ID前缀的NI物品
    val search = subCommand {
        // ni search
        execute<CommandSender> { sender, _, _ ->
            async {
                Help.help(sender)
            }
        }
        // ni search [ID前缀]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                arrayListOf("idPrefix")
            }
            execute<CommandSender> { sender, _, argument ->
                searchCommandAsync(sender, argument, 1)
            }
            // ni search [ID前缀] (页码)
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, context ->
                    (1..ceil(
                        ItemManager.itemIds.filter { id -> id.startsWith(context.argument(-1)) }.size.toDouble() / ConfigManager.config.getDouble(
                            "ItemList.ItemAmount"
                        )
                    ).toInt()).toList().map { it.toString() }
                }
                execute<CommandSender> { sender, context, argument ->
                    searchCommandAsync(sender, context.argument(-1), argument.toIntOrNull() ?: 1)
                }
            }
        }
    }

    private fun searchCommandAsync(
        sender: CommandSender,
        idPrefix: String,
        page: Int
    ) {
        async {
            listCommand(sender, idPrefix, page)
        }
    }

    private fun listCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // id前缀
        idPrefix: String,
        // 页码
        page: Int
    ) {
        val ids = ItemManager.itemIds.filter { id -> id.startsWith(idPrefix) }
        val pageAmount = ceil(ids.size.toDouble() / ConfigManager.config.getDouble("ItemList.ItemAmount")).toInt()
        val realPage = page.coerceAtMost(pageAmount).coerceAtLeast(1)
        // 发送前缀
        ConfigManager.config.getString("ItemList.Prefix")?.let { sender.sendMessage(it) }
        // 预构建待发送信息
        val listMessage = ComponentBuilder("")
        // 获取当前序号
        val prevItemAmount = ((realPage - 1) * ConfigManager.config.getInt("ItemList.ItemAmount")) + 1
        // 逐个获取物品
        for (index in (prevItemAmount until prevItemAmount + ConfigManager.config.getInt("ItemList.ItemAmount"))) {
            if (index == ids.size + 1) break
            val id = ids[index - 1]
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
                        else -> Material.matchMaterial(
                            (itemKeySection?.getString("material") ?: "").uppercase(Locale.getDefault())
                        )
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
                .runCommand("/ni search $idPrefix ${realPage - 1}")
        }
        val nextRaw = ComponentBuilder(ConfigManager.config.getString("ItemList.Next") ?: "")
        if (realPage != pageAmount) {
            nextRaw.hoverText((ConfigManager.config.getString("ItemList.Next") ?: "") + ": " + (realPage + 1))
            nextRaw.runCommand("/ni search $idPrefix ${realPage + 1}")
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
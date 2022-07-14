package pers.neige.neigeitems.command

import org.bukkit.entity.Player
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common.util.sync
import taboolib.expansion.createHelper
import taboolib.platform.util.giveItem

@CommandHeader(name = "NeigeItems", aliases = ["ni"])
object Command {
    @CommandBody
    val main = mainCommand {
        createHelper()
        incorrectSender { sender, _ ->
            config.getString("Messages.onlyPlayer")?.let { sender.sendMessage(it) }
        }
        incorrectCommand { sender, _, index, _ ->
            when (index) {
                1 -> {
                    config.getStringList("Messages.helpMessages").forEach {
                        sender.sendMessage(it)
                    }
                }
            }
        }
    }

    @CommandBody
    val test = subCommand {
        dynamic(commit = "item") {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            dynamic(optional = true, commit = "amount") {
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        argument.toIntOrNull()?.let { amount ->
                            repeat((1..amount).count()) {
                                submit(async = true) {
                                    ItemManager.getItemStack(context.argument(-1), sender) ?: let {
                                        sender.sendMessage(
                                            config.getString("Messages.unknownItem")?.replace("{itemID}", argument)
                                        )
                                    }
                                }
                            }
                        } ?: let {
                            sender.sendMessage(config.getString("Messages.invalidAmount"))
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    // ni get [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID获取NI物品
    val get = subCommand {
        // ni get [物品ID]
        dynamic(commit = "item") {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    // 给物品
                    ItemManager.getItemStack(argument, sender)?.let { itemStack ->
                        sync { sender.giveItem(itemStack) }
                        // 未知物品ID
                    } ?: let {
                        sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", argument))
                    }
                }
            }
            // ni get [物品ID] (数量)
            dynamic(optional = true, commit = "amount") {
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        // 获取数量
                        argument.toIntOrNull()?.let { amount ->
                            // 给物品
                            repeat((1..amount).count()) {
                                submit(async = true) {
                                    ItemManager.getItemStack(context.argument(-1), sender)?.let { itemStack ->
                                        sync { sender.giveItem(itemStack) }
                                        // 未知物品ID
                                    } ?: let {
                                        sender.sendMessage(
                                            config.getString("Messages.unknownItem")?.replace("{itemID}", argument)
                                        )
                                    }
                                }
                            }
                            // 无效数字
                        } ?: let {
                            sender.sendMessage(config.getString("Messages.invalidAmount"))
                        }
                    }
                }
                // ni get [物品ID] (数量) (是否反复随机)
                dynamic(optional = true, commit = "random") {

                }
            }
        }
    }
}
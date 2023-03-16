package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.event.ItemGiveEvent
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getItemTag

object Give {
    // ni get [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID获取NI物品
    val get = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni get [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<Player> { sender, _, argument ->
                giveCommandAsync(sender, sender, argument, "1")
            }
            // ni get [物品ID] (数量)
            dynamic(optional = true) {
                suggestion<Player>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<Player> { sender, context, argument ->
                    giveCommandAsync(sender, sender, context.argument(-1), argument)
                }
                // ni get [物品ID] (数量) (是否反复随机)
                dynamic(optional = true) {
                    suggestion<Player>(uncheck = true) { _, _ ->
                        arrayListOf("true", "false")
                    }
                    execute<Player> { sender, context, argument ->
                        giveCommandAsync(sender, sender, context.argument(-2), context.argument(-1), argument)
                    }
                    // ni get [物品ID] (数量) (是否反复随机) (指向数据)
                    dynamic(optional = true) {
                        suggestion<Player>(uncheck = true) { _, _ ->
                            arrayListOf("data")
                        }
                        execute<Player> { sender, context, argument ->
                            giveCommandAsync(sender, sender, context.argument(-3), context.argument(-2), context.argument(-1), argument)
                        }
                    }
                }
            }
        }
    }

    // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予NI物品
    val give = subCommand {
        execute<CommandSender> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<CommandSender> { sender, _, _ ->
                submit(async = true) {
                    help(sender)
                }
            }
            // ni give [玩家ID] [物品ID]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    ItemManager.items.keys.toList()
                }
                execute<CommandSender> { sender, context, argument ->
                    giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-1)), argument, "1")
                }
                // ni give [玩家ID] [物品ID] (数量)
                dynamic(optional = true) {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("amount")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-2)), context.argument(-1), argument)
                    }
                    // ni give [玩家ID] [物品ID] (数量) (是否反复随机)
                    dynamic(optional = true) {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("true", "false")
                        }
                        execute<CommandSender> { sender, context, argument ->
                            giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-3)), context.argument(-2), context.argument(-1), argument)
                        }
                        // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据)
                        dynamic(optional = true) {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("data")
                            }
                            execute<CommandSender> { sender, context, argument ->
                                giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-4)), context.argument(-3), context.argument(-2), context.argument(-1), argument)
                            }
                        }
                    }
                }
            }
        }
    }

    // ni giveAll [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予所有人NI物品
    val giveAll = subCommand {
        execute<CommandSender> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni giveAll [物品ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<CommandSender> { sender, _, argument ->
                giveAllCommandAsync(sender, argument, "1")
            }
            // ni giveAll [物品ID] (数量)
            dynamic(optional = true) {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, context, argument ->
                    giveAllCommandAsync(sender, context.argument(-1), argument)
                }
                // ni giveAll [物品ID] (数量) (是否反复随机)
                dynamic(optional = true) {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("true", "false")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        giveAllCommandAsync(sender, context.argument(-2), context.argument(-1), argument)
                    }
                    // ni giveAll [物品ID] (数量) (是否反复随机) (指向数据)
                    dynamic(optional = true) {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("data")
                        }
                        execute<CommandSender> { sender, context, argument ->
                            giveAllCommandAsync(sender, context.argument(-3), context.argument(-2), context.argument(-1), argument)
                        }
                    }
                }
            }
        }
    }

    private fun giveCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 物品接收者
        player: Player?,
        // 待给予物品ID
        id: String,
        // 给予数量
        amount: String?,
        // 是否反复随机
        random: String?,
        // 指向数据
        data: String?
    ) {
        giveCommand(sender, player, id, amount?.toIntOrNull(), random, data)
    }

    private fun giveCommandAsync(
        sender: CommandSender,
        player: Player?,
        id: String,
        amount: String? = null,
        random: String? = null,
        data: String? = null
    ) {
        submit(async = true) {
            giveCommand(sender, player, id, amount, random, data)
        }
    }

    private fun giveAllCommandAsync(
        sender: CommandSender,
        id: String,
        amount: String? = null,
        random: String? = null,
        data: String? = null
    ) {
        submit(async = true) {
            Bukkit.getOnlinePlayers().forEach { player ->
                giveCommand(sender, player, id, amount, random, data)
            }
        }
    }

    private fun giveCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        amount: Int?,
        random: String?,
        data: String?
    ) {
        player?.let {
            when (random) {
                "false", "0" -> {
                    // 获取数量
                    amount?.let {
                        // 给物品
                        ItemManager.getItemStack(id, player, data)?.let { itemStack ->
                            // 移除一下物品拥有者信息
                            // 我知道这种操作有些sb, 但是暂时别无他法
                            if (ConfigManager.removeNBTWhenGive) {
                                val itemTag = itemStack.getItemTag()
                                val neigeItems = itemTag["NeigeItems"]?.asCompound()
                                neigeItems?.get("owner")?.asString()?.let {
                                    neigeItems.remove("owner")
                                    itemTag.saveTo(itemStack)
                                }
                            }
                            // 物品给予事件
                            val event = ItemGiveEvent(id, player, itemStack, amount.coerceAtLeast(1))
                            event.call()
                            if (event.isCancelled) return
                            NeigeItems.bukkitScheduler.callSyncMethod(NeigeItems.plugin) {
                                player.giveItems(event.itemStack, event.amount)
                            }
                            sender.sendLang("Messages.successInfo", mapOf(
                                Pair("{player}", player.name),
                                Pair("{amount}", amount.toString()),
                                Pair("{name}", itemStack.getParsedName())
                            ))
                            player.sendLang("Messages.givenInfo", mapOf(
                                Pair("{amount}", amount.toString()),
                                Pair("{name}", itemStack.getParsedName())
                            ))
                            // 未知物品ID
                        } ?: let {
                            sender.sendLang("Messages.unknownItem", mapOf(
                                Pair("{itemID}", id)
                            ))
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendLang("Messages.invalidAmount")
                    }
                }
                else -> {
                    // 获取数量
                    amount?.let {
                        val dropData = HashMap<String, Int>()
                        // 给物品
                        if (ItemManager.hasItem(id)) {
                            repeat(amount.coerceAtLeast(1)) {
                                ItemManager.getItemStack(id, player, data)?.let letItem@ { itemStack ->
                                    // 移除一下物品拥有者信息
                                    // 我知道这种操作有些sb, 但是暂时别无他法
                                    if (ConfigManager.removeNBTWhenGive) {
                                        val itemTag = itemStack.getItemTag()
                                        val neigeItems = itemTag["NeigeItems"]?.asCompound()
                                        neigeItems?.get("owner")?.asString()?.let {
                                            neigeItems.remove("owner")
                                            itemTag.saveTo(itemStack)
                                        }
                                    }
                                    // 物品给予事件
                                    val event = ItemGiveEvent(id, player, itemStack, 1)
                                    event.call()
                                    if (event.isCancelled) return@letItem
                                    NeigeItems.bukkitScheduler.callSyncMethod(NeigeItems.plugin) {
                                        player.giveItems(event.itemStack, event.amount)
                                    }
                                    dropData[event.itemStack.getParsedName()] = dropData[event.itemStack.getParsedName()]?.let { it + event.amount } ?: event.amount
                                    // 未知物品ID
                                }
                            }
                        } else {
                            sender.sendLang("Messages.unknownItem", mapOf(
                                Pair("{itemID}", id)
                            ))
                        }
                        for ((name, amt) in dropData) {
                            sender.sendLang("Messages.successInfo", mapOf(
                                Pair("{player}", player.name),
                                Pair("{amount}", amt.toString()),
                                Pair("{name}", name)
                            ))
                            player.sendLang("Messages.givenInfo", mapOf(
                                Pair("{amount}", amt.toString()),
                                Pair("{name}", name)
                            ))
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendLang("Messages.invalidAmount")
                    }
                }
            }
            // 无效玩家
        } ?: let {
            sender.sendLang("Messages.invalidPlayer")
        }
    }
}
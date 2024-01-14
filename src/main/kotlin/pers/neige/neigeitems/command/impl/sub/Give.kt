package pers.neige.neigeitems.command.impl.sub

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.CommandDispatcher.command
import pers.neige.neigeitems.command.subcommand.Help
import pers.neige.neigeitems.event.ItemGiveEvent
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull
import pers.neige.neigeitems.utils.ItemUtils.saveToSafe
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import pers.neige.neigeitems.utils.SchedulerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand

object Give {
    // ni get [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID获取NI物品
    val get = command("get")
        .execute {
            async {
                Help.help(it.sender)
            }
        }
        // ni get [物品ID]
        .then(command("物品ID", true)
            .suggest {
                ItemManager.items.keys.toList()
            }
            .execute {
                giveCommandAsync(it.sender, it.sender, it.arg!!, "1")
            }
            // ni get [物品ID] (数量)
            .then(command("数量", true)
                .suggest {
                    arrayListOf("amount")
                }
                .execute {
                    giveCommandAsync(it.sender, it.sender, it.get("物品ID")!!, it.arg)
                }
                // ni get [物品ID] (数量) (是否反复随机)
                .then(command("是否反复随机", true)
                    .suggest {
                        arrayListOf("true", "false")
                    }
                    .execute {
                        giveCommandAsync(it.sender, it.sender, it.get("物品ID")!!, it.get("数量")!!, it.arg)
                    }
                    // ni get [物品ID] (数量) (是否反复随机) (指向数据)
                    .then(command("指向数据", true)
                        .suggest {
                            arrayListOf("data")
                        }
                        .execute {
                            giveCommandAsync(
                                it.sender,
                                it.sender,
                                it.get("物品ID")!!,
                                it.get("数量")!!,
                                it.get("是否反复随机")!!,
                                it.arg
                            )
                        }
                    )
                )
            )
        )

    // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予NI物品
    val give = subCommand {
        execute<CommandSender> { sender, _, _ ->
            async {
                Help.help(sender)
            }
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<CommandSender> { sender, _, _ ->
                async {
                    Help.help(sender)
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
                        giveCommandAsync(
                            sender,
                            Bukkit.getPlayerExact(context.argument(-2)),
                            context.argument(-1),
                            argument
                        )
                    }
                    // ni give [玩家ID] [物品ID] (数量) (是否反复随机)
                    dynamic(optional = true) {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("true", "false")
                        }
                        execute<CommandSender> { sender, context, argument ->
                            giveCommandAsync(
                                sender,
                                Bukkit.getPlayerExact(context.argument(-3)),
                                context.argument(-2),
                                context.argument(-1),
                                argument
                            )
                        }
                        // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据)
                        dynamic(optional = true) {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("data")
                            }
                            execute<CommandSender> { sender, context, argument ->
                                giveCommandAsync(
                                    sender,
                                    Bukkit.getPlayerExact(context.argument(-4)),
                                    context.argument(-3),
                                    context.argument(-2),
                                    context.argument(-1),
                                    argument
                                )
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
            SchedulerUtils.async {
                Help.help(sender)
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
                            giveAllCommandAsync(
                                sender,
                                context.argument(-3),
                                context.argument(-2),
                                context.argument(-1),
                                argument
                            )
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
        player: CommandSender?,
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
        player: CommandSender?,
        id: String,
        amount: String? = null,
        random: String? = null,
        data: String? = null
    ) {
        async {
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
        async {
            Bukkit.getOnlinePlayers().forEach { player ->
                giveCommand(sender, player, id, amount, random, data)
            }
        }
    }

    private fun giveCommand(
        sender: CommandSender,
        player: CommandSender?,
        id: String,
        amount: Int?,
        random: String?,
        data: String?
    ) {
        player?.let {
            if (player !is Player) {
                sender.sendLang("Messages.onlyPlayer")
                return
            }
            when (random) {
                "false", "0" -> {
                    // 获取数量
                    amount?.let {
                        // 给物品
                        ItemManager.getItemStack(id, player, data)?.let { itemStack ->
                            // 移除一下物品拥有者信息
                            if (ConfigManager.removeNBTWhenGive) {
                                val nbt = itemStack.getNbtOrNull()
                                if (nbt != null) {
                                    nbt.getCompound("NeigeItems")?.remove("owner")
                                    nbt.saveToSafe(itemStack)
                                }
                            }
                            // 物品给予事件
                            val event = ItemGiveEvent(id, player, itemStack, amount.coerceAtLeast(1))
                            event.call()
                            if (event.isCancelled) return
                            SchedulerUtils.sync {
                                player.giveItems(event.itemStack, event.amount)
                            }
                            sender.sendLang(
                                "Messages.successInfo", mapOf(
                                    Pair("{player}", player.name),
                                    Pair("{amount}", amount.toString()),
                                    Pair("{name}", itemStack.getParsedName())
                                )
                            )
                            player.sendLang(
                                "Messages.givenInfo", mapOf(
                                    Pair("{amount}", amount.toString()),
                                    Pair("{name}", itemStack.getParsedName())
                                )
                            )
                            // 未知物品ID
                        } ?: let {
                            sender.sendLang(
                                "Messages.unknownItem", mapOf(
                                    Pair("{itemID}", id)
                                )
                            )
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
                                ItemManager.getItemStack(id, player, data)?.let letItem@{ itemStack ->
                                    // 移除一下物品拥有者信息
                                    if (ConfigManager.removeNBTWhenGive) {
                                        val nbt = itemStack.getNbtOrNull()
                                        if (nbt != null) {
                                            nbt.getCompound("NeigeItems")?.remove("owner")
                                            nbt.saveToSafe(itemStack)
                                        }
                                    }
                                    // 物品给予事件
                                    val event = ItemGiveEvent(id, player, itemStack, 1)
                                    event.call()
                                    if (event.isCancelled) return@letItem
                                    SchedulerUtils.sync {
                                        player.giveItems(event.itemStack, event.amount)
                                    }
                                    dropData[event.itemStack.getParsedName()] =
                                        dropData[event.itemStack.getParsedName()]?.let { it + event.amount }
                                            ?: event.amount
                                    // 未知物品ID
                                }
                            }
                        } else {
                            sender.sendLang(
                                "Messages.unknownItem", mapOf(
                                    Pair("{itemID}", id)
                                )
                            )
                        }
                        for ((name, amt) in dropData) {
                            sender.sendLang(
                                "Messages.successInfo", mapOf(
                                    Pair("{player}", player.name),
                                    Pair("{amount}", amt.toString()),
                                    Pair("{name}", name)
                                )
                            )
                            player.sendLang(
                                "Messages.givenInfo", mapOf(
                                    Pair("{amount}", amt.toString()),
                                    Pair("{name}", name)
                                )
                            )
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
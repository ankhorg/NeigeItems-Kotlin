package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.event.ItemDropEvent
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.dropNiItems
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand

object Drop {
    val drop = subCommand {
        // ni drop [物品ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<CommandSender> { sender, _, _ ->
                async {
                    help(sender)
                }
            }
            // ni drop [物品ID] [数量]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, _, _ ->
                    async {
                        help(sender)
                    }
                }
                // ni drop [物品ID] [数量] [世界名]
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        Bukkit.getWorlds().map { it.name }
                    }
                    execute<CommandSender> { sender, _, _ ->
                        async {
                            help(sender)
                        }
                    }
                    // ni drop [物品ID] [数量] [世界名] [X坐标]
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("x")
                        }
                        execute<CommandSender> { sender, _, _ ->
                            async {
                                help(sender)
                            }
                        }
                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标]
                        dynamic {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("y")
                            }
                            execute<CommandSender> { sender, _, _ ->
                                async {
                                    help(sender)
                                }
                            }
                            // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标]
                            dynamic {
                                suggestion<CommandSender>(uncheck = true) { _, _ ->
                                    arrayListOf("z")
                                }
                                execute<CommandSender> { sender, _, _ ->
                                    async {
                                        help(sender)
                                    }
                                }
                                // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机]
                                dynamic {
                                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                                        arrayListOf("true", "false")
                                    }
                                    execute<CommandSender> { sender, _, _ ->
                                        async {
                                            help(sender)
                                        }
                                    }
                                    // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象]
                                    dynamic {
                                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                                            Bukkit.getOnlinePlayers().map { it.name }
                                        }
                                        execute<CommandSender> { sender, context, argument ->
                                            dropCommandAsync(
                                                sender,
                                                context.argument(-7),
                                                context.argument(-6),
                                                context.argument(-5),
                                                context.argument(-4),
                                                context.argument(-3),
                                                context.argument(-2),
                                                context.argument(-1),
                                                argument
                                            )
                                        }
                                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象] (指向数据)
                                        dynamic(optional = true) {
                                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                                arrayListOf("data")
                                            }
                                            execute<CommandSender> { sender, context, argument ->
                                                dropCommandAsync(
                                                    sender,
                                                    context.argument(-8),
                                                    context.argument(-7),
                                                    context.argument(-6),
                                                    context.argument(-5),
                                                    context.argument(-4),
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
                }
            }
        }
    }

    val dropSilent = subCommand {
        // ni drop [物品ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<CommandSender> { sender, _, _ ->
                async {
                    help(sender)
                }
            }
            // ni drop [物品ID] [数量]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, _, _ ->
                    async {
                        help(sender)
                    }
                }
                // ni drop [物品ID] [数量] [世界名]
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        Bukkit.getWorlds().map { it.name }
                    }
                    execute<CommandSender> { sender, _, _ ->
                        async {
                            help(sender)
                        }
                    }
                    // ni drop [物品ID] [数量] [世界名] [X坐标]
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("x")
                        }
                        execute<CommandSender> { sender, _, _ ->
                            async {
                                help(sender)
                            }
                        }
                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标]
                        dynamic {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("y")
                            }
                            execute<CommandSender> { sender, _, _ ->
                                async {
                                    help(sender)
                                }
                            }
                            // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标]
                            dynamic {
                                suggestion<CommandSender>(uncheck = true) { _, _ ->
                                    arrayListOf("z")
                                }
                                execute<CommandSender> { sender, _, _ ->
                                    async {
                                        help(sender)
                                    }
                                }
                                // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机]
                                dynamic {
                                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                                        arrayListOf("true", "false")
                                    }
                                    execute<CommandSender> { sender, _, _ ->
                                        async {
                                            help(sender)
                                        }
                                    }
                                    // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象]
                                    dynamic {
                                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                                            Bukkit.getOnlinePlayers().map { it.name }
                                        }
                                        execute<CommandSender> { sender, context, argument ->
                                            dropCommandAsync(
                                                sender,
                                                context.argument(-7),
                                                context.argument(-6),
                                                context.argument(-5),
                                                context.argument(-4),
                                                context.argument(-3),
                                                context.argument(-2),
                                                context.argument(-1),
                                                argument,
                                                tip = false
                                            )
                                        }
                                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象] (指向数据)
                                        dynamic(optional = true) {
                                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                                arrayListOf("data")
                                            }
                                            execute<CommandSender> { sender, context, argument ->
                                                dropCommandAsync(
                                                    sender,
                                                    context.argument(-8),
                                                    context.argument(-7),
                                                    context.argument(-6),
                                                    context.argument(-5),
                                                    context.argument(-4),
                                                    context.argument(-3),
                                                    context.argument(-2),
                                                    context.argument(-1),
                                                    argument,
                                                    tip = false
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dropCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 待掉落物品ID
        id: String,
        // 掉落数量
        amount: String,
        // 掉落世界名
        worldName: String,
        // 掉落世界x坐标
        xString: String,
        // 掉落世界y坐标
        yString: String,
        // 掉落世界z坐标
        zString: String,
        // 是否反复随机
        random: String,
        // 物品解析对象, 用于生成物品
        parser: String,
        // 指向数据
        data: String?,
        // 是否进行消息提示
        tip: Boolean
    ) {
        Bukkit.getWorld(worldName)?.let { world ->
            val x = xString.toDoubleOrNull()
            val y = yString.toDoubleOrNull()
            val z = zString.toDoubleOrNull()
            if (x != null && y != null && z != null) {
                dropCommand(
                    sender,
                    id,
                    amount.toIntOrNull(),
                    Location(world, x, y, z),
                    random,
                    Bukkit.getPlayerExact(parser),
                    data,
                    tip
                )
            } else {
                sender.sendLang("Messages.invalidLocation")
            }
        } ?: let {
            sender.sendLang("Messages.invalidWorld")
        }
    }

    private fun dropCommandAsync(
        sender: CommandSender,
        id: String,
        amount: String,
        worldName: String,
        xString: String,
        yString: String,
        zString: String,
        random: String,
        parser: String,
        data: String? = null,
        tip: Boolean = true
    ) {
        async {
            dropCommand(sender, id, amount, worldName, xString, yString, zString, random, parser, data, tip)
        }
    }

    private fun dropCommand(
        sender: CommandSender,
        id: String,
        amount: Int?,
        location: Location,
        random: String,
        parser: Player?,
        data: String?,
        tip: Boolean
    ) {
        parser?.let {
            when (random) {
                "false", "0" -> {
                    // 获取数量
                    amount?.let {
                        // 掉物品
                        ItemManager.getItemStack(id, parser, data)?.also { itemStack ->
                            // 物品掉落事件
                            val event = ItemDropEvent(id, itemStack, amount.coerceAtLeast(1), location, parser)
                            event.call()
                            if (event.isCancelled) return
                            event.location.dropNiItems(event.itemStack, event.amount, parser)
                            if (tip) {
                                sender.sendLang(
                                    "Messages.dropSuccessInfo", mapOf(
                                        Pair("{world}", event.location.world?.name ?: ""),
                                        Pair("{x}", event.location.x.toString()),
                                        Pair("{y}", event.location.y.toString()),
                                        Pair("{z}", event.location.z.toString()),
                                        Pair("{amount}", event.amount.toString()),
                                        Pair("{name}", event.itemStack.getParsedName())
                                    )
                                )
                            }
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
                        // 防止有人在ItemDropEvent瞎几把改location以后提示出错
                        val dropData = HashMap<Location, HashMap<String, Int>>()
                        // 掉物品
                        repeat(amount.coerceAtLeast(1)) {
                            ItemManager.getItemStack(id, parser, data)?.also alsoItem@{ itemStack ->
                                // 物品掉落事件
                                val event = ItemDropEvent(id, itemStack, 1, location, parser)
                                event.call()
                                if (event.isCancelled) return@alsoItem
                                event.location.dropNiItems(event.itemStack, event.amount, parser)
                                val currentData = dropData.computeIfAbsent(event.location) { HashMap() }
                                currentData[event.itemStack.getParsedName()] =
                                    currentData[event.itemStack.getParsedName()]?.let { it + event.amount }
                                        ?: event.amount
                                // 未知物品ID
                            } ?: let {
                                sender.sendLang(
                                    "Messages.unknownItem", mapOf(
                                        Pair("{itemID}", id)
                                    )
                                )
                                return@repeat
                            }
                        }
                        if (tip) {
                            for ((loc, currentData) in dropData) {
                                for ((name, amt) in currentData) {
                                    sender.sendLang(
                                        "Messages.dropSuccessInfo", mapOf(
                                            Pair("{world}", loc.world?.name ?: ""),
                                            Pair("{x}", loc.x.toString()),
                                            Pair("{y}", loc.y.toString()),
                                            Pair("{z}", loc.z.toString()),
                                            Pair("{amount}", amt.toString()),
                                            Pair("{name}", name)
                                        )
                                    )
                                }
                            }
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendLang("Messages.invalidAmount")
                    }
                }
            }
            // 未知解析对象
        } ?: let {
            sender.sendLang("Messages.invalidParser")
        }
    }
}
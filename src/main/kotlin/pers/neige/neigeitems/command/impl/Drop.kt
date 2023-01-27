package pers.neige.neigeitems.command.impl

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.impl.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.dropNiItem
import pers.neige.neigeitems.utils.ItemUtils.dropNiItems
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName
import taboolib.platform.util.sendLang

object Drop {
    val drop = subCommand {
        // ni drop [物品ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<CommandSender> { sender, _, _ ->
                submit(async = true) {
                    help(sender)
                }
            }
            // ni drop [物品ID] [数量]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, _, _ ->
                    submit(async = true) {
                        help(sender)
                    }
                }
                // ni drop [物品ID] [数量] [世界名]
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        Bukkit.getWorlds().map { it.name }
                    }
                    execute<CommandSender> { sender, _, _ ->
                        submit(async = true) {
                            help(sender)
                        }
                    }
                    // ni drop [物品ID] [数量] [世界名] [X坐标]
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("x")
                        }
                        execute<CommandSender> { sender, _, _ ->
                            submit(async = true) {
                                help(sender)
                            }
                        }
                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标]
                        dynamic {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("y")
                            }
                            execute<CommandSender> { sender, _, _ ->
                                submit(async = true) {
                                    help(sender)
                                }
                            }
                            // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标]
                            dynamic {
                                suggestion<CommandSender>(uncheck = true) { _, _ ->
                                    arrayListOf("z")
                                }
                                execute<CommandSender> { sender, _, _ ->
                                    submit(async = true) {
                                        help(sender)
                                    }
                                }
                                // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机]
                                dynamic {
                                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                                        arrayListOf("true", "false")
                                    }
                                    execute<CommandSender> { sender, _, _ ->
                                        submit(async = true) {
                                            help(sender)
                                        }
                                    }
                                    // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象]
                                    dynamic {
                                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                                            Bukkit.getOnlinePlayers().map { it.name }
                                        }
                                        execute<CommandSender> { sender, context, argument ->
                                            dropCommandAsync(sender, context.argument(-7), context.argument(-6), context.argument(-5), context.argument(-4), context.argument(-3), context.argument(-2), context.argument(-1), argument)
                                        }
                                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象] (指向数据)
                                        dynamic(optional = true) {
                                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                                arrayListOf("data")
                                            }
                                            execute<CommandSender> { sender, context, argument ->
                                                dropCommandAsync(sender, context.argument(-8), context.argument(-7), context.argument(-6), context.argument(-5), context.argument(-4), context.argument(-3), context.argument(-2), context.argument(-1), argument)
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
        data: String?
    ) {
        Bukkit.getWorld(worldName)?.let { world ->
            val x = xString.toDoubleOrNull()
            val y = yString.toDoubleOrNull()
            val z = zString.toDoubleOrNull()
            if (x != null && y != null && z != null) {
                dropCommand(sender, id, amount.toIntOrNull(), Location(world, x, y, z), random, Bukkit.getPlayerExact(parser), data)
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
        data: String? = null
    ) {
        submit(async = true) {
            dropCommand(sender, id, amount, worldName, xString, yString, zString, random, parser, data)
        }
    }

    private fun dropCommand(
        sender: CommandSender,
        id: String,
        amount: Int?,
        location: Location?,
        random: String,
        parser: Player?,
        data: String?
    ) {
        parser?.let {
            when (random) {
                "false", "0" -> {
                    // 获取数量
                    amount?.let {
                        // 掉物品
                        ItemManager.getItemStack(id, parser, data)?.let { itemStack ->
                            location?.dropNiItems(itemStack, amount.coerceAtLeast(1), parser)
                            sender.sendLang("Messages.dropSuccessInfo", mapOf(
                                Pair("{world}", location?.world?.name ?: ""),
                                Pair("{x}", location?.x.toString()),
                                Pair("{y}", location?.y.toString()),
                                Pair("{z}", location?.z.toString()),
                                Pair("{amount}", amount.toString()),
                                Pair("{name}", itemStack.getName())
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
                        // 掉物品
                        repeat(amount.coerceAtLeast(1)) {
                            ItemManager.getItemStack(id, parser, data)?.let { itemStack ->
                                location?.dropNiItem(itemStack, parser)
                                dropData[itemStack.getName()] = dropData[itemStack.getName()]?.let { it + 1 } ?: let { 1 }
                                // 未知物品ID
                            } ?: let {
                                sender.sendLang("Messages.unknownItem", mapOf(
                                    Pair("{itemID}", id)
                                ))
                                return@repeat
                            }
                        }
                        for((name, amt) in dropData) {
                            sender.sendLang("Messages.dropSuccessInfo", mapOf(
                                Pair("{world}", location?.world?.name ?: ""),
                                Pair("{x}", location?.x.toString()),
                                Pair("{y}", location?.y.toString()),
                                Pair("{z}", location?.z.toString()),
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
            // 未知解析对象
        } ?: let {
            sender.sendLang("Messages.invalidParser")
        }
    }
}
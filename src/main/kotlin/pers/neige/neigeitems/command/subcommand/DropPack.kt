package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.event.ItemPackDropEvent
import pers.neige.neigeitems.manager.ItemPackManager
import pers.neige.neigeitems.utils.ItemUtils.dropItems
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand

object DropPack {
    val dropPack = subCommand {
        // ni dropPack [物品包ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemPackManager.itemPackIds
            }
            execute<CommandSender> { sender, _, _ ->
                async {
                    help(sender)
                }
            }
            // ni dropPack [物品包ID] [数量]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, _, _ ->
                    async {
                        help(sender)
                    }
                }
                // ni dropPack [物品包ID] [数量] [世界名]
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        Bukkit.getWorlds().map { it.name }
                    }
                    execute<CommandSender> { sender, _, _ ->
                        async {
                            help(sender)
                        }
                    }
                    // ni dropPack [物品包ID] [数量] [世界名] [X坐标]
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("x")
                        }
                        execute<CommandSender> { sender, _, _ ->
                            async {
                                help(sender)
                            }
                        }
                        // ni dropPack [物品包ID] [数量] [世界名] [X坐标] [Y坐标]
                        dynamic {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("y")
                            }
                            execute<CommandSender> { sender, _, _ ->
                                async {
                                    help(sender)
                                }
                            }
                            // ni dropPack [物品包ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标]
                            dynamic {
                                suggestion<CommandSender>(uncheck = true) { _, _ ->
                                    arrayListOf("z")
                                }
                                execute<CommandSender> { sender, _, _ ->
                                    async {
                                        help(sender)
                                    }
                                }
                                // ni dropPack [物品包ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [物品解析对象]
                                dynamic {
                                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                                        Bukkit.getOnlinePlayers().map { it.name }
                                    }
                                    execute<CommandSender> { sender, context, argument ->
                                        dropPackCommandAsync(sender, context.argument(-6), context.argument(-5), context.argument(-4), context.argument(-3), context.argument(-2), context.argument(-1), argument)
                                    }
                                    // ni dropPack [物品包ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [物品解析对象] (指向数据)
                                    dynamic {
                                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                                            arrayListOf("data")
                                        }
                                        execute<CommandSender> { sender, context, argument ->
                                            dropPackCommandAsync(sender, context.argument(-7), context.argument(-6), context.argument(-5), context.argument(-4), context.argument(-3), context.argument(-2), context.argument(-1), argument)
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
    
    private fun dropPackCommandAsync(
        sender: CommandSender,
        id: String,
        repeat: String?,
        worldName: String,
        xString: String,
        yString: String,
        zString: String,
        parser: String,
        data: String? = null
    ) {
        async {
            dropPackCommand(sender, id, repeat, worldName, xString, yString, zString, parser, data)
        }
    }

    private fun dropPackCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 待掉落物品组ID
        id: String,
        // 重复次数
        repeat: String?,
        // 掉落世界名
        worldName: String,
        // 掉落世界x坐标
        xString: String,
        // 掉落世界y坐标
        yString: String,
        // 掉落世界z坐标
        zString: String,
        // 物品解析对象, 用于生成物品
        parser: String,
        // 指向数据
        data: String? = null
    ) {
        Bukkit.getWorld(worldName)?.let { world ->
            val x = xString.toDoubleOrNull()
            val y = yString.toDoubleOrNull()
            val z = zString.toDoubleOrNull()
            if (x != null && y != null && z != null) {
                dropPackCommand(sender, id, repeat?.toIntOrNull(), Location(world, x, y, z), Bukkit.getPlayerExact(parser), data)
            } else {
                sender.sendLang("Messages.invalidLocation")
            }
        } ?: let {
            sender.sendLang("Messages.invalidWorld")
        }
    }

    private fun dropPackCommand(
        sender: CommandSender,
        id: String,
        repeat: Int?,
        location: Location,
        parser: Player?,
        data: String? = null
    ) {
        parser?.let {
            ItemPackManager.getItemPack(id)?.let { itemPack ->
                val packInfo = HashMap<Location, Int>()
                repeat(repeat ?: 1) {
                    // 预定于掉落物列表
                    val dropItems = ArrayList<ItemStack>()
                    // 加载掉落信息
                    dropItems.addAll(itemPack.getItemStacks(parser, data))
                    // 物品包掉落事件
                    val event = ItemPackDropEvent(id, dropItems, location, parser)
                    event.call()
                    if (!event.isCancelled) {
                        event.location.let { location ->
                            if (itemPack.fancyDrop) {
                                dropItems(
                                    event.itemStacks,
                                    location,
                                    parser,
                                    itemPack.offsetXString,
                                    itemPack.offsetYString,
                                    itemPack.angleType
                                )
                            } else {
                                dropItems(event.itemStacks, location, parser)
                            }
                        }
                        packInfo[event.location] = (packInfo[event.location] ?: 0) + 1
                    }
                }
                for ((loc, amount) in packInfo) {
                    sender.sendLang("Messages.dropPackSuccessInfo", mapOf(
                        Pair("{world}", loc.world?.name ?: ""),
                        Pair("{x}", loc.x.toString()),
                        Pair("{y}", loc.y.toString()),
                        Pair("{z}", loc.z.toString()),
                        Pair("{amount}", amount.toString()),
                        Pair("{name}", id)
                    ))
                }
                // 未知物品包
            } ?: let {
                sender.sendLang("Messages.unknownItemPack", mapOf(
                    Pair("{packID}", id)
                ))
            }
            // 未知解析对象
        } ?: let {
            sender.sendLang("Messages.invalidParser")
        }
    }
}
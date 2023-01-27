package pers.neige.neigeitems.command.impl

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.impl.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ItemPackManager
import pers.neige.neigeitems.utils.ItemUtils
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName
import taboolib.platform.util.sendLang

object DropPack {
    val dropPack = subCommand {
        // ni dropPack [物品包ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemPackManager.itemPackIds
            }
            execute<CommandSender> { sender, _, _ ->
                submit(async = true) {
                    help(sender)
                }
            }
            // ni dropPack [物品包ID] [数量]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, _, _ ->
                    submit(async = true) {
                        help(sender)
                    }
                }
                // ni dropPack [物品包ID] [数量] [世界名]
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        Bukkit.getWorlds().map { it.name }
                    }
                    execute<CommandSender> { sender, _, _ ->
                        submit(async = true) {
                            help(sender)
                        }
                    }
                    // ni dropPack [物品包ID] [数量] [世界名] [X坐标]
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("x")
                        }
                        execute<CommandSender> { sender, _, _ ->
                            submit(async = true) {
                                help(sender)
                            }
                        }
                        // ni dropPack [物品包ID] [数量] [世界名] [X坐标] [Y坐标]
                        dynamic {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("y")
                            }
                            execute<CommandSender> { sender, _, _ ->
                                submit(async = true) {
                                    help(sender)
                                }
                            }
                            // ni dropPack [物品包ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标]
                            dynamic {
                                suggestion<CommandSender>(uncheck = true) { _, _ ->
                                    arrayListOf("z")
                                }
                                execute<CommandSender> { sender, _, _ ->
                                    submit(async = true) {
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
        parser: String
    ) {
        submit(async = true) {
            dropPackCommand(sender, id, repeat, worldName, xString, yString, zString, parser)
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
        parser: String
    ) {
        Bukkit.getWorld(worldName)?.let { world ->
            val x = xString.toDoubleOrNull()
            val y = yString.toDoubleOrNull()
            val z = zString.toDoubleOrNull()
            if (x != null && y != null && z != null) {
                dropPackCommand(sender, id, repeat?.toIntOrNull(), Location(world, x, y, z), Bukkit.getPlayerExact(parser))
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
        location: Location?,
        parser: Player?
    ) {
        parser?.let {
            ItemPackManager.itemPacks[id]?.let { itemPack ->
                repeat(repeat ?: 1) {
                    // 预定于掉落物列表
                    val dropItems = ArrayList<ItemStack>()
                    // 加载掉落信息
                    ItemUtils.loadItems(dropItems, itemPack.items, parser, HashMap<String, String>(), itemPack.sections)
                    location?.let { location ->
                        if (itemPack.fancyDrop) {
                            ItemUtils.dropItems(
                                dropItems,
                                location,
                                parser,
                                itemPack.offsetXString,
                                itemPack.offsetYString,
                                itemPack.angleType
                            )
                        } else {
                            ItemUtils.dropItems(dropItems, location, parser)
                        }
                    }
                }
                sender.sendLang("Messages.dropPackSuccessInfo", mapOf(
                    Pair("{world}", location?.world?.name ?: ""),
                    Pair("{x}", location?.x.toString()),
                    Pair("{y}", location?.y.toString()),
                    Pair("{z}", location?.z.toString()),
                    Pair("{amount}", repeat.toString()),
                    Pair("{name}", id)
                ))
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
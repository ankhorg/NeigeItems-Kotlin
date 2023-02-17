package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.event.ItemPackGiveEvent
import pers.neige.neigeitems.manager.ItemPackManager
import pers.neige.neigeitems.utils.ItemUtils
import pers.neige.neigeitems.utils.LangUtils.getLang
import pers.neige.neigeitems.utils.LangUtils.sendLang
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName
import taboolib.platform.util.giveItem

object GivePack {
    // ni givePack [玩家ID] [物品包ID] (数量) > 根据ID给予NI物品包
    val givePack = subCommand {
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
            // ni givePack [玩家ID] [物品包ID]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    ItemPackManager.itemPackIds
                }
                execute<CommandSender> { sender, context, argument ->
                    givePackCommandAsync(sender, context.argument(-1), argument, "1")
                }
                // ni givePack [玩家ID] [物品包ID] (数量)
                dynamic(optional = true) {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("amount")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        givePackCommandAsync(sender, context.argument(-2), context.argument(-1), argument)
                    }
                }
            }
        }
    }

    private fun givePackCommandAsync(
        sender: CommandSender,
        player: String,
        id: String,
        repeat: String?
    ) {
        submit(async = true) {
            givePackCommand(sender, player, id, repeat)
        }
    }

    private fun givePackCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 给予对象
        player: String,
        // 待给予物品组ID
        id: String,
        // 重复次数
        repeat: String?
    ) {
        givePackCommand(sender, Bukkit.getPlayerExact(player), id, repeat?.toIntOrNull())
    }

    private fun givePackCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        repeat: Int?
    ) {
        player?.let {
            ItemPackManager.itemPacks[id]?.let { itemPack ->
                // 如果是按物品提示, 就建立map存储信息
                val dropData = when (getLang("Messages.type.givePackMessage")) {
                    "Items" -> HashMap<String, Int>()
                    else -> null
                }
                repeat(repeat?.coerceAtLeast(1) ?: 1) {
                    // 预定于掉落物列表
                    val dropItems = ArrayList<ItemStack>()
                    // 加载掉落信息
                    ItemUtils.loadItems(dropItems, itemPack.items, player, HashMap(), itemPack.sections)
                    // 物品包给予事件
                    val event = ItemPackGiveEvent(id, player, dropItems)
                    event.call()
                    if (!event.isCancelled) {
                        event.itemStacks.forEach { itemStack ->
                            bukkitScheduler.callSyncMethod(plugin) {
                                player.giveItem(itemStack)
                            }
                            dropData?.let {
                                dropData[itemStack.getName()] = dropData[itemStack.getName()]?.let { it + 1 } ?: let { 1 }
                            }
                        }
                    }
                }
                // 信息提示
                dropData?.let {
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
                } ?: let {
                    sender.sendLang("Messages.successPackInfo", mapOf(
                        Pair("{player}", player.name),
                        Pair("{amount}", repeat.toString()),
                        Pair("{name}", id)
                    ))
                    player.sendLang("Messages.givenPackInfo", mapOf(
                        Pair("{amount}", repeat.toString()),
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
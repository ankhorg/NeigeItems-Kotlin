package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName

object MMGive {
    // ni mm get [物品ID] (数量) > 根据ID获取MM物品
    val get = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni mm get [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                mythicMobsHooker!!.getItemIds()
            }
            execute<Player> { sender, _, argument ->
                giveAddonCommandAsync(
                    sender,
                    sender,
                    argument,
                    mythicMobsHooker!!.getItemStack(argument),
                    1
                )
            }
            // ni mm get [物品ID] (数量)
            dynamic(optional = true) {
                suggestion<Player>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<Player> { sender, context, argument ->
                    giveAddonCommandAsync(
                        sender,
                        sender,
                        context.argument(-1),
                        mythicMobsHooker!!.getItemStack(context.argument(-1)),
                        argument.toIntOrNull()
                    )
                }
            }
        }
    }

    // ni mm give [玩家ID] [物品ID] (数量) > 根据ID给予MM物品
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
            // ni mm give [玩家ID] [物品ID]
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    mythicMobsHooker!!.getItemIds()
                }
                execute<CommandSender> { sender, context, argument ->
                    giveAddonCommandAsync(
                        sender,
                        Bukkit.getPlayerExact(context.argument(-1)),
                        argument,
                        mythicMobsHooker!!.getItemStack(argument),
                        1
                    )
                }
                // ni give [玩家ID] [物品ID] (数量)
                dynamic(optional = true) {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("amount")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        giveAddonCommandAsync(
                            sender,
                            Bukkit.getPlayerExact(context.argument(-2)),
                            context.argument(-1),
                            mythicMobsHooker!!.getItemStack(context.argument(-1)),
                            argument.toIntOrNull()
                        )
                    }
                }
            }
        }
    }

    // ni mm giveAll [物品ID] (数量) > 根据ID给予所有人MM物品
    val giveAll = subCommand {
        execute<CommandSender> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni mm giveAll [物品ID]
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                mythicMobsHooker!!.getItemIds()
            }
            execute<CommandSender> { sender, _, argument ->
                giveAddonAllCommandAsync(
                    sender,
                    argument,
                    mythicMobsHooker!!.getItemStack(argument),
                    1
                )
            }
            // ni giveAll [物品ID] (数量)
            dynamic(optional = true) {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, context, argument ->
                    giveAddonAllCommandAsync(
                        sender,
                        context.argument(-1),
                        mythicMobsHooker!!.getItemStack(context.argument(-1)),
                        argument.toIntOrNull()
                    )
                }
            }
        }
    }

    private fun giveAddonAllCommandAsync(
        sender: CommandSender,
        id: String,
        itemStack: ItemStack?,
        amount: Int?
    ) {
        submit(async = true) {
            Bukkit.getOnlinePlayers().forEach { player ->
                giveAddonCommand(
                    sender,
                    player,
                    id,
                    itemStack,
                    amount
                )
            }
        }
    }

    private fun giveAddonCommandAsync(
        sender: CommandSender,
        player: Player?,
        id: String,
        itemStack: ItemStack?,
        amount: Int?
    ) {
        submit (async = true) {
            giveAddonCommand(
                sender,
                player,
                id,
                itemStack,
                amount
            )
        }
    }

    private fun giveAddonCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        itemStack: ItemStack?,
        amount: Int?
    ) {
        player?.let {
            // 获取数量
            amount?.let {
                // 给物品
                itemStack?.let {
                    Bukkit.getScheduler().callSyncMethod(NeigeItems.plugin) {
                        player.giveItems(itemStack, amount.coerceAtLeast(1))
                    }
                    sender.sendLang("Messages.successInfo", mapOf(
                        Pair("{player}", player.name),
                        Pair("{amount}", amount.toString()),
                        Pair("{name}", itemStack.getName())
                    ))
                    player.sendLang("Messages.givenInfo", mapOf(
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
            // 无效玩家
        } ?: let {
            sender.sendLang("Messages.invalidPlayer")
        }
    }
}
package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.positiveInteger
import pers.neige.neigeitems.command.arguments.ItemPackArgumentType.getItemPackSelector
import pers.neige.neigeitems.command.arguments.ItemPackArgumentType.pack
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayerSelector
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.ItemPackSelector
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.event.ItemPackGiveEvent
import pers.neige.neigeitems.item.ItemPack
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.utils.ItemUtils.removeOwnerNbt
import pers.neige.neigeitems.utils.LangUtils.getLang
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItem
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SchedulerUtils.sync

object GivePack {
    private val givePackLogic: RequiredArgumentBuilder<CommandSender, PlayerSelector> =
        // ni givePack [player]
        argument<CommandSender, PlayerSelector>("player", player()).then(
            // ni givePack [player] [pack]
            argument<CommandSender, ItemPackSelector>("pack", pack()).executes { context ->
                givePack(context)
            }.then(
                // ni givePack [player] [pack] (amount)
                argument<CommandSender, Int>("amount", positiveInteger()).executes { context ->
                    givePack(context, getInteger(context, "amount"))
                }.then(
                    // ni givePack [player] [pack] (amount) (data)
                    argument<CommandSender, String>("data", greedyString()).executes { context ->
                        givePack(
                            context,
                            getInteger(context, "amount"),
                            getString(context, "data")
                        )
                    }
                )
            )
        )

    // ni givePack
    val givePack: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("givePack").then(givePackLogic)

    // ni givePackSilent
    val givePackSilent: LiteralArgumentBuilder<CommandSender> =
        literal<CommandSender>("givePackSilent").then(givePackLogic)

    private fun givePack(
        context: CommandContext<CommandSender>,
        amount: Int = 1,
        data: String? = null
    ): Int {
        async {
            val tip = !context.nodes[0].node.name.endsWith("Silent")
            val sender = context.source
            val itemPackSelector = getItemPackSelector(context, "pack")
            val itemPack = itemPackSelector.getPack(context) ?: let {
                sender.sendLang("Messages.unknownItemPack", mapOf(Pair("{packID}", itemPackSelector.id)))
                return@async
            }
            val playerSelector = getPlayerSelector(context, "player")
            val player = playerSelector.getPlayer(context) ?: let {
                sender.sendLang("Messages.invalidPlayer", mapOf(Pair("{player}", playerSelector.name)))
                return@async
            }
            givePackCommand(
                sender, player, itemPack, amount, data, tip
            )
        }
        return 1
    }

    fun givePackCommand(
        sender: CommandSender,
        player: Player,
        itemPack: ItemPack,
        repeat: Int,
        data: String? = null,
        tip: Boolean
    ) {
        // 如果是按物品提示, 就建立map存储信息
        val dropData = when (getLang("Messages.type.givePackMessage")) {
            "Items" -> HashMap<String, Int>()
            else -> null
        }
        repeat(repeat) {
            // 预定于掉落物列表
            val itemStacks = ArrayList<ItemStack>()
            // 加载掉落信息
            itemStacks.addAll(itemPack.getItemStacks(player, data))
            // 物品包给予事件
            val event = ItemPackGiveEvent(itemPack.id, player, itemStacks)
            event.call()
            if (!event.isCancelled) {
                event.itemStacks.forEach { itemStack ->
                    // 移除一下物品拥有者信息
                    if (ConfigManager.removeNBTWhenGive) {
                        itemStack.removeOwnerNbt()
                    }
                    sync {
                        player.giveItem(itemStack)
                    }
                    val name = itemStack.getParsedName()
                    dropData?.merge(name, itemStack.amount, Integer::sum)
                }
            }
        }
        // 信息提示
        if (tip) {
            if (dropData == null) {
                sender.sendLang(
                    "Messages.successPackInfo", mapOf(
                        Pair("{player}", player.name),
                        Pair("{amount}", repeat.toString()),
                        Pair("{name}", itemPack.id)
                    )
                )
                player.sendLang(
                    "Messages.givenPackInfo", mapOf(
                        Pair("{amount}", repeat.toString()),
                        Pair("{name}", itemPack.id)
                    )
                )
            } else {
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
            }
        }
    }
}
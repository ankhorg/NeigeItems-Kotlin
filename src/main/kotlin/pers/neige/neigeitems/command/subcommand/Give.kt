package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.arguments.BoolArgumentType.bool
import com.mojang.brigadier.arguments.BoolArgumentType.getBool
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.CommandUtils
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.positiveInteger
import pers.neige.neigeitems.command.arguments.ItemArgumentType.getItemSelector
import pers.neige.neigeitems.command.arguments.ItemArgumentType.item
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayerSelector
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.ItemSelector
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.event.ItemGiveEvent
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.utils.ItemUtils.removeOwnerNbt
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SchedulerUtils.sync

object Give {
    private val getLogic: RequiredArgumentBuilder<CommandSender, ItemSelector> =
        // ni get [item]
        argument<CommandSender, ItemSelector>("item", item()).executes { context ->
            give(context)
        }.then(
            // ni get [item] (amount)
            argument<CommandSender, Int>("amount", positiveInteger()).executes { context ->
                give(context, getInteger(context, "amount"))
            }.then(
                // ni get [item] (amount) (random)
                argument<CommandSender, Boolean>("random", bool()).executes { context ->
                    give(context, getInteger(context, "amount"), getBool(context, "random"))
                }.then(
                    // ni get [item] (amount) (random) (data)
                    argument<CommandSender, String>("data", greedyString()).executes { context ->
                        give(
                            context,
                            getInteger(context, "amount"),
                            getBool(context, "random"),
                            getString(context, "data")
                        )
                    }
                )
            )
        )

    private val giveLogic: RequiredArgumentBuilder<CommandSender, PlayerSelector> =
        // ni give [player]
        argument<CommandSender, PlayerSelector>("player", player()).then(getLogic)

    // ni get
    val get: LiteralArgumentBuilder<CommandSender> = CommandUtils.literal<CommandSender>("get").then(getLogic)

    // ni getSilent
    val getSilent: LiteralArgumentBuilder<CommandSender> =
        CommandUtils.literal<CommandSender>("getSilent").then(getLogic)

    // ni give
    val give: LiteralArgumentBuilder<CommandSender> = CommandUtils.literal<CommandSender>("give").then(giveLogic)

    // ni giveSilent
    val giveSilent: LiteralArgumentBuilder<CommandSender> =
        CommandUtils.literal<CommandSender>("giveSilent").then(giveLogic)

    // ni giveAll
    val giveAll: LiteralArgumentBuilder<CommandSender> = CommandUtils.literal<CommandSender>("giveAll").then(getLogic)

    // ni giveAllSilent
    val giveAllSilent: LiteralArgumentBuilder<CommandSender> =
        CommandUtils.literal<CommandSender>("giveAllSilent").then(getLogic)

    private fun give(
        context: CommandContext<CommandSender>,
        amount: Int = 1,
        random: Boolean = true,
        data: String? = null
    ): Int {
        async {
            val get = context.nodes[0].node.name.startsWith("get")
            val all = context.nodes[0].node.name.startsWith("giveAll")
            val tip = !context.nodes[0].node.name.endsWith("Silent")
            val sender = context.source
            if (get && sender !is Player) {
                sender.sendLang("Messages.onlyPlayer")
                return@async
            }
            val itemSelector = getItemSelector(context, "item")
            val item = itemSelector.getItem(context) ?: let {
                sender.sendLang("Messages.unknownItem", mapOf(Pair("{itemID}", itemSelector.id)))
                return@async
            }
            if (all) {
                Bukkit.getOnlinePlayers().forEach { player ->
                    giveCommand(
                        sender, player, item, amount, random, data, tip
                    )
                }
            } else {
                val player = if (get) {
                    sender as Player
                } else {
                    val playerSelector = getPlayerSelector(context, "player")
                    playerSelector.getPlayer(context) ?: let {
                        sender.sendLang("Messages.invalidPlayer", mapOf(Pair("{player}", playerSelector.name)))
                        return@async
                    }
                }
                giveCommand(
                    sender, player, item, amount, random, data, tip
                )
            }
        }
        return 1
    }

    private fun giveCommand(
        sender: CommandSender,
        player: Player,
        item: ItemGenerator,
        amount: Int,
        random: Boolean,
        data: String?,
        tip: Boolean
    ) {
        val giveData = HashMap<String, Int>()
        if (random) {
            // 给物品
            repeat(amount.coerceAtLeast(1)) {
                item.getItemStack(player, data)?.let { itemStack ->
                    // 移除一下物品拥有者信息
                    if (ConfigManager.removeNBTWhenGive) {
                        itemStack.removeOwnerNbt()
                    }
                    // 物品给予事件
                    val event = ItemGiveEvent(item.id, player, itemStack, 1)
                    event.call()
                    if (event.isCancelled) return@let
                    sync {
                        player.giveItems(event.itemStack, event.amount)
                    }
                    giveData[event.itemStack.getParsedName()] =
                        giveData.getOrDefault(event.itemStack.getParsedName(), 0) + event.amount
                }
            }
        } else {
            val itemStack = item.getItemStack(player, data) ?: return
            // 移除一下物品拥有者信息
            if (ConfigManager.removeNBTWhenGive) {
                itemStack.removeOwnerNbt()
            }
            // 物品给予事件
            val event = ItemGiveEvent(item.id, player, itemStack, amount.coerceAtLeast(1))
            event.call()
            if (event.isCancelled) return
            sync {
                player.giveItems(event.itemStack, event.amount)
            }
            giveData[event.itemStack.getParsedName()] = event.amount
        }
        if (tip) {
            giveData.forEach { (name, amount) ->
                sender.sendLang(
                    "Messages.successInfo", mapOf(
                        Pair("{player}", player.name),
                        Pair("{amount}", amount.toString()),
                        Pair("{name}", name)
                    )
                )
                player.sendLang(
                    "Messages.givenInfo", mapOf(
                        Pair("{amount}", amount.toString()),
                        Pair("{name}", name)
                    )
                )
            }
        }
    }
}
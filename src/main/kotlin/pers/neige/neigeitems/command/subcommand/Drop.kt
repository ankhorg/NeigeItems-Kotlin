package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.arguments.BoolArgumentType.bool
import com.mojang.brigadier.arguments.BoolArgumentType.getBool
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.positiveInteger
import pers.neige.neigeitems.command.arguments.ItemArgumentType.getItemSelector
import pers.neige.neigeitems.command.arguments.ItemArgumentType.item
import pers.neige.neigeitems.command.arguments.LocationArgumentType.getLocation
import pers.neige.neigeitems.command.arguments.LocationArgumentType.location
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayerSelector
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.arguments.WorldArgumentType.getWorldSelector
import pers.neige.neigeitems.command.arguments.WorldArgumentType.world
import pers.neige.neigeitems.command.coordinates.Coordinates
import pers.neige.neigeitems.command.selector.ItemSelector
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.command.selector.WorldSelector
import pers.neige.neigeitems.event.ItemDropEvent
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.utils.ItemUtils.dropNiItems
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

object Drop {
    private val dropLogic: RequiredArgumentBuilder<CommandSender, ItemSelector> =
        // ni drop [item]
        argument<CommandSender, ItemSelector>("item", item()).then(
            // ni drop [item] [amount]
            argument<CommandSender, Int>("amount", positiveInteger()).then(
                // ni drop [item] [amount] [world]
                argument<CommandSender, WorldSelector>("world", world()).then(
                    // ni drop [item] [amount] [world] [location]
                    argument<CommandSender, Coordinates>("location", location()).executes { context ->
                        drop(context)
                    }.then(
                        // ni drop [item] [amount] [world] [location] (random)
                        argument<CommandSender, Boolean>("random", bool()).executes { context ->
                            drop(context, getBool(context, "random"))
                        }.then(
                            // ni drop [item] [amount] [world] [location] (random) (target)
                            argument<CommandSender, PlayerSelector>("target", player()).executes { context ->
                                drop(
                                    context, getBool(context, "random"), getPlayerSelector(context, "target")
                                )
                            }.then(
                                // ni drop [item] [amount] [world] [location] (random) (target) (data)
                                argument<CommandSender, String>("data", greedyString()).executes { context ->
                                    drop(
                                        context,
                                        getBool(context, "random"),
                                        getPlayerSelector(context, "target"),
                                        getString(context, "data")
                                    )
                                }
                            )
                        )
                    )
                )
            )
        )

    // ni drop
    val drop: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("drop").then(dropLogic)

    // ni dropSilent
    val dropSilent: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("dropSilent").then(dropLogic)

    fun drop(
        context: CommandContext<CommandSender>,
        random: Boolean = true,
        parserSelector: PlayerSelector? = null,
        data: String? = null
    ): Int {
        async {
            val tip = context.nodes[0].node.name == "drop"
            val sender = context.source

            val itemSelector = getItemSelector(context, "item")
            val item = itemSelector.getItem(context) ?: let {
                sender.sendLang("Messages.unknownItem", mapOf(Pair("{itemID}", itemSelector.id)))
                return@async
            }

            val amount = getInteger(context, "amount")

            val worldSelector = getWorldSelector(context, "world")
            val world = worldSelector.getWorld(context) ?: let {
                sender.sendLang("Messages.invalidWorld", mapOf(Pair("{world}", worldSelector.name)))
                return@async
            }

            val location = getLocation(world, context, "location") ?: let {
                sender.sendLang("Messages.invalidLocation")
                return@async
            }

            val parser = if (parserSelector == null) {
                null
            } else {
                parserSelector.getPlayer(context) ?: let {
                    sender.sendLang("Messages.invalidPlayer", mapOf(Pair("{player}", parserSelector.name)))
                    return@async
                }
            }

            dropCommand(
                sender, item, amount, location, random, parser, data, tip
            )
        }
        return 1
    }

    fun dropCommand(
        sender: CommandSender,
        item: ItemGenerator,
        amount: Int,
        location: Location,
        random: Boolean = true,
        parser: Player? = null,
        data: String? = null,
        tip: Boolean
    ) {
        // 防止有人在ItemDropEvent改location以后提示出错
        val dropData = HashMap<Location, HashMap<String, Int>>()
        if (random) {
            // 掉物品
            repeat(amount.coerceAtLeast(1)) {
                item.getItemStack(parser, data)?.also alsoItem@{ itemStack ->
                    // 物品掉落事件
                    val event = ItemDropEvent(item.id, itemStack, 1, location, parser)
                    event.call()
                    if (event.isCancelled) return@alsoItem
                    event.location.dropNiItems(event.itemStack, event.amount, parser)
                    val currentData = dropData.computeIfAbsent(event.location) { HashMap() }
                    currentData.merge(event.itemStack.getParsedName(), event.amount, Integer::sum)
                }
            }
        } else {
            // 掉物品
            item.getItemStack(parser, data)?.also { itemStack ->
                // 物品掉落事件
                val event = ItemDropEvent(item.id, itemStack, amount.coerceAtLeast(1), location, parser)
                event.call()
                if (event.isCancelled) return
                event.location.dropNiItems(event.itemStack, event.amount, parser)
                val currentData = dropData.computeIfAbsent(event.location) { HashMap() }
                currentData[event.itemStack.getParsedName()] = event.amount
            }
        }
        if (tip) {
            for ((loc, currentData) in dropData) {
                for ((name, amt) in currentData) {
                    sender.sendLang(
                        "Messages.dropSuccessInfo", mapOf(
                            Pair("{world}", loc.world?.name ?: ""),
                            Pair("{x}", String.format("%.2f", loc.x)),
                            Pair("{y}", String.format("%.2f", loc.y)),
                            Pair("{z}", String.format("%.2f", loc.z)),
                            Pair("{amount}", amt.toString()),
                            Pair("{name}", name),
                            Pair("{player}", parser?.name ?: "null")
                        )
                    )
                }
            }
        }
    }
}
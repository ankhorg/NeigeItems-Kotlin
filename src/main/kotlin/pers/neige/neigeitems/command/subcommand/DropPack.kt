package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.positiveInteger
import pers.neige.neigeitems.command.arguments.ItemPackArgumentType.getItemPackSelector
import pers.neige.neigeitems.command.arguments.ItemPackArgumentType.pack
import pers.neige.neigeitems.command.arguments.LocationArgumentType.getLocation
import pers.neige.neigeitems.command.arguments.LocationArgumentType.location
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayerSelector
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.arguments.WorldArgumentType.getWorldSelector
import pers.neige.neigeitems.command.arguments.WorldArgumentType.world
import pers.neige.neigeitems.command.coordinates.Coordinates
import pers.neige.neigeitems.command.selector.ItemPackSelector
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.command.selector.WorldSelector
import pers.neige.neigeitems.event.ItemPackDropEvent
import pers.neige.neigeitems.item.ItemPack
import pers.neige.neigeitems.utils.ItemUtils.dropItems
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni droppack指令
 */
object DropPack {
    private val dropPackLogic: RequiredArgumentBuilder<CommandSender, ItemPackSelector> =
        // ni dropPack [pack]
        argument<CommandSender, ItemPackSelector>("pack", pack()).then(
            // ni dropPack [pack] [amount]
            argument<CommandSender, Int>("amount", positiveInteger()).then(
                // ni dropPack [pack] [amount] [world]
                argument<CommandSender, WorldSelector>("world", world()).then(
                    // ni dropPack [pack] [amount] [world] [location]
                    argument<CommandSender, Coordinates>("location", location()).executes { context ->
                        dropPack(context)
                    }.then(
                        // ni dropPack [pack] [amount] [world] [location] (target)
                        argument<CommandSender, PlayerSelector>("target", player()).executes { context ->
                            dropPack(
                                context,
                                getPlayerSelector(context, "target")
                            )
                        }.then(
                            // ni dropPack [pack] [amount] [world] [location] (target) (data)
                            argument<CommandSender, String>(
                                "data",
                                StringArgumentType.greedyString()
                            ).executes { context ->
                                dropPack(
                                    context,
                                    getPlayerSelector(context, "target"),
                                    StringArgumentType.getString(context, "data")
                                )
                            }
                        )
                    )
                )
            )
        )

    // ni dropPack
    val dropPack: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("dropPack").then(dropPackLogic)

    // ni dropPackSilent
    val dropPackSilent: LiteralArgumentBuilder<CommandSender> =
        literal<CommandSender>("dropPackSilent").then(dropPackLogic)

    private fun dropPack(
        context: CommandContext<CommandSender>,
        parserSelector: PlayerSelector? = null,
        data: String? = null
    ): Int {
        async {
            val tip = context.nodes[0].node.name == "dropPack"
            val sender = context.source

            val itemPackSelector = getItemPackSelector(context, "pack")
            val pack = itemPackSelector.select(context) ?: let {
                sender.sendLang("Messages.unknownItemPack", mapOf(Pair("{packID}", itemPackSelector.text)))
                return@async
            }

            val amount = getInteger(context, "amount")

            val worldSelector = getWorldSelector(context, "world")
            val world = worldSelector.select(context) ?: let {
                sender.sendLang("Messages.invalidWorld", mapOf(Pair("{world}", worldSelector.text)))
                return@async
            }

            val location = getLocation(world, context, "location") ?: let {
                sender.sendLang("Messages.invalidLocation")
                return@async
            }

            val parser = if (parserSelector == null) {
                null
            } else {
                parserSelector.select(context) ?: let {
                    sender.sendLang("Messages.invalidPlayer", mapOf(Pair("{player}", parserSelector.text)))
                    return@async
                }
            }

            dropPackCommand(
                sender, pack, amount, location, parser, data, tip
            )
        }
        return 1
    }

    fun dropPackCommand(
        sender: CommandSender,
        itemPack: ItemPack,
        repeat: Int,
        location: Location,
        parser: Player? = null,
        data: String? = null,
        tip: Boolean
    ) {
        val packInfo = HashMap<Location, Int>()
        repeat(repeat) {
            // 预定于掉落物列表
            val dropItems = ArrayList<ItemStack>()
            // 加载掉落信息
            dropItems.addAll(itemPack.getItemStacks(parser, data))
            // 物品包掉落事件
            val event = ItemPackDropEvent(itemPack.id, dropItems, location, parser)
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
                packInfo.merge(event.location, 1, Integer::sum)
            }
        }
        if (tip) {
            for ((loc, amount) in packInfo) {
                sender.sendLang(
                    "Messages.dropPackSuccessInfo", mapOf(
                        Pair("{world}", loc.world?.name ?: ""),
                        Pair("{x}", String.format("%.2f", loc.x)),
                        Pair("{y}", String.format("%.2f", loc.y)),
                        Pair("{z}", String.format("%.2f", loc.z)),
                        Pair("{amount}", amount.toString()),
                        Pair("{name}", itemPack.id),
                        Pair("{player}", parser?.name ?: "null")
                    )
                )
            }
        }
    }
}
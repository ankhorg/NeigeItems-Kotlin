package pers.neige.neigeitems.command.subcommand

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.argument
import pers.neige.colonel.arguments.impl.BooleanArgument
import pers.neige.colonel.arguments.impl.StringArgument
import pers.neige.colonel.coordinates.CoordinatesContainer
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.*
import pers.neige.neigeitems.event.ItemDropEvent
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.utils.ItemUtils.dropNiItems
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni drop指令
 */
object Drop {
    @JvmStatic
    @CustomField(fieldType = "root")
    val drop = literal<CommandSender, Unit>("drop", arrayListOf("drop", "dropSilent")) {
        argument("item", ItemArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                argument("world", WorldArgument.INSTANCE) {
                    argument("location", CoordinatesArgument.INSTANCE) {
                        argument("random", BooleanArgument<CommandSender, Unit>().setDefaultValue(true)) {
                            argument("target", PlayerArgument.NONNULL.setDefaultValue(null)) {
                                argument(
                                    "data",
                                    StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                                        .setDefaultValue(null)
                                ) {
                                    setNullExecutor { context ->
                                        async {
                                            val sender = context.source ?: return@async
                                            val tip = context.getArgument<String>("drop").equals("drop", true)
                                            val item =
                                                context.getArgument<ItemArgument.ItemContainer>("item").itemGenerator!!
                                            val amount = context.getArgument<Int?>("amount")!!
                                            val world = context.getArgument<World>("world")!!
                                            val location =
                                                context.getArgument<CoordinatesContainer>("location")!!.result!!.getLocation(
                                                    world,
                                                    sender as? Player
                                                )
                                            val random = context.getArgument<Boolean?>("random")!!
                                            val parser = context.getArgument<Player>("target")
                                            val data = context.getArgument<String>("data")
                                            dropCommand(
                                                sender, item, amount, location, random, parser, data, tip
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

    fun dropCommand(
        sender: CommandSender,
        item: ItemGenerator,
        amount: Int,
        location: Location,
        random: Boolean,
        parser: Player?,
        data: String?,
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
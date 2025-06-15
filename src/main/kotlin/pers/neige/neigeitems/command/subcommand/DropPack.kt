package pers.neige.neigeitems.command.subcommand

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.colonel.argument
import pers.neige.colonel.arguments.impl.StringArgument
import pers.neige.colonel.coordinates.CoordinatesContainer
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.*
import pers.neige.neigeitems.event.ItemPackDropEvent
import pers.neige.neigeitems.item.ItemPack
import pers.neige.neigeitems.utils.ItemUtils.dropItems
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni droppack指令
 */
object DropPack {
    @JvmStatic
    @CustomField(fieldType = "root")
    val dropPack = literal<CommandSender, Unit>("dropPack", arrayListOf("dropPack", "dropPackSilent")) {
        argument("pack", ItemPackArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                argument("world", WorldArgument.INSTANCE) {
                    argument("location", CoordinatesArgument.INSTANCE) {
                        argument("target", PlayerArgument.NONNULL.setDefaultValue(null)) {
                            argument(
                                "data",
                                StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                                    .setDefaultValue(null)
                            ) {
                                setNullExecutor { context ->
                                    async {
                                        val sender = context.source ?: return@async
                                        val tip = context.getArgument<String>("dropPack").equals("dropPack", true)
                                        val pack =
                                            context.getArgument<ItemPackArgument.ItemPackContainer>("pack").itemPack!!
                                        val amount = context.getArgument<Int?>("amount")!!
                                        val world = context.getArgument<World>("world")!!
                                        val location =
                                            context.getArgument<CoordinatesContainer>("location")!!.result!!.getLocation(
                                                world,
                                                sender as? Player
                                            )
                                        val parser = context.getArgument<Player>("target")
                                        val data = context.getArgument<String>("data")
                                        dropPackCommand(
                                            sender, pack, amount, location, parser, data, tip
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
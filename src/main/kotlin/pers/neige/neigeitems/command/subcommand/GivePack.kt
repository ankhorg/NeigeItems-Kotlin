package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.colonel.argument
import pers.neige.colonel.arguments.impl.StringArgument
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.IntegerArgument
import pers.neige.neigeitems.colonel.argument.command.ItemPackArgument
import pers.neige.neigeitems.colonel.argument.command.PlayerArgument
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

/**
 * ni givepack指令
 */
object GivePack {
    @JvmStatic
    @CustomField(fieldType = "root")
    val givePack = literal<CommandSender, Unit>("givePack", arrayListOf("givePack", "givePackSilent")) {
        argument("player", PlayerArgument.NONNULL) {
            argument("pack", ItemPackArgument.INSTANCE) {
                argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                    argument(
                        "data",
                        StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                            .setDefaultValue(null)
                    ) {
                        setNullExecutor { context ->
                            async {
                                val sender = context.source ?: return@async
                                val tip = context.getArgument<String>("givePack").equals("givePack", true)
                                val player = context.getArgument<Player>("player")!!
                                val itemPack =
                                    context.getArgument<ItemPackArgument.ItemPackContainer>("pack").itemPack!!
                                val amount = context.getArgument<Int?>("amount")!!
                                val data = context.getArgument<String>("data")
                                givePackCommand(
                                    sender, player, itemPack, amount, data, tip
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    @CustomField(fieldType = "root")
    val givePackAll = literal<CommandSender, Unit>("givePackAll", arrayListOf("givePackAll", "givePackAllSilent")) {
        argument("pack", ItemPackArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                argument(
                    "data",
                    StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                        .setDefaultValue(null)
                ) {
                    setNullExecutor { context ->
                        async {
                            val sender = context.source ?: return@async
                            val tip = context.getArgument<String>("givePackAll").equals("givePackAll", true)
                            val itemPack =
                                context.getArgument<ItemPackArgument.ItemPackContainer>("pack").itemPack!!
                            val amount = context.getArgument<Int?>("amount")!!
                            val data = context.getArgument<String>("data")
                            Bukkit.getOnlinePlayers().forEach { player ->
                                givePackCommand(
                                    sender, player, itemPack, amount, data, tip
                                )
                            }
                        }
                    }
                }
            }
        }
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
package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.argument
import pers.neige.colonel.arguments.impl.BooleanArgument
import pers.neige.colonel.arguments.impl.StringArgument
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.IntegerArgument
import pers.neige.neigeitems.colonel.argument.command.ItemArgument
import pers.neige.neigeitems.colonel.argument.command.PlayerArgument
import pers.neige.neigeitems.event.ItemGiveEvent
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.utils.ItemUtils.removeOwnerNbt
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SchedulerUtils.sync

/**
 * ni give指令
 */
object Give {
    @JvmStatic
    @CustomField(fieldType = "root")
    val get = literal<CommandSender, Unit>("get", arrayListOf("get", "getSilent")) {
        argument("item", ItemArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                argument("random", BooleanArgument<CommandSender, Unit>().setDefaultValue(true)) {
                    argument(
                        "data",
                        StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                            .setDefaultValue(null)
                    ) {
                        setNullExecutor { context ->
                            async {
                                val sender = context.source ?: return@async
                                if (sender !is Player) {
                                    sender.sendLang("Messages.onlyPlayer")
                                    return@async
                                }
                                val tip = context.getArgument<String>("get").equals("get", true)
                                val item = context.getArgument<ItemArgument.ItemContainer>("item").itemGenerator!!
                                val amount = context.getArgument<Int?>("amount")!!
                                val random = context.getArgument<Boolean?>("random")!!
                                val data = context.getArgument<String>("data")
                                giveCommand(
                                    sender, sender, item, amount, random, data, tip
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
    val give = literal<CommandSender, Unit>("give", arrayListOf("give", "giveSilent")) {
        argument("player", PlayerArgument.NONNULL) {
            argument("item", ItemArgument.INSTANCE) {
                argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                    argument("random", BooleanArgument<CommandSender, Unit>().setDefaultValue(true)) {
                        argument(
                            "data",
                            StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                                .setDefaultValue(null)
                        ) {
                            setNullExecutor { context ->
                                async {
                                    val sender = context.source ?: return@async
                                    val tip = context.getArgument<String>("give").equals("give", true)
                                    val player = context.getArgument<Player>("player")!!
                                    val item = context.getArgument<ItemArgument.ItemContainer>("item").itemGenerator!!
                                    val amount = context.getArgument<Int?>("amount")!!
                                    val random = context.getArgument<Boolean?>("random")!!
                                    val data = context.getArgument<String>("data")
                                    giveCommand(
                                        sender, player, item, amount, random, data, tip
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    @CustomField(fieldType = "root")
    val giveAll = literal<CommandSender, Unit>("giveAll", arrayListOf("giveAll", "giveAllSilent")) {
        argument("item", ItemArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                argument("random", BooleanArgument<CommandSender, Unit>().setDefaultValue(true)) {
                    argument(
                        "data",
                        StringArgument.builder<CommandSender, Unit>().readAll(true).build()
                            .setDefaultValue(null)
                    ) {
                        setNullExecutor { context ->
                            async {
                                val sender = context.source ?: return@async
                                val tip = context.getArgument<String>("giveAll").equals("giveAll", true)
                                val item = context.getArgument<ItemArgument.ItemContainer>("item").itemGenerator!!
                                val amount = context.getArgument<Int?>("amount")!!
                                val random = context.getArgument<Boolean?>("random")!!
                                val data = context.getArgument<String>("data")
                                Bukkit.getOnlinePlayers().forEach { player ->
                                    giveCommand(
                                        sender, player, item, amount, random, data, tip
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun giveCommand(
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
                    giveData.merge(event.itemStack.getParsedName(), event.amount, Integer::sum)
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
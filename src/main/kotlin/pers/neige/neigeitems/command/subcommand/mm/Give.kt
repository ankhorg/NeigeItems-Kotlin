package pers.neige.neigeitems.command.subcommand.mm

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.argument
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.IntegerArgument
import pers.neige.neigeitems.colonel.argument.command.MaybeMMItemIdArgument
import pers.neige.neigeitems.colonel.argument.command.PlayerArgument
import pers.neige.neigeitems.manager.HookerManager.getParsedName
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SchedulerUtils.sync

/**
 * ni mm give指令
 */
object Give {
    @JvmStatic
    @CustomField(fieldType = "mm")
    val get = literal<CommandSender, Unit>("get", arrayListOf("get", "getSilent")) {
        argument("itemId", MaybeMMItemIdArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                setNullExecutor { context ->
                    async {
                        val sender = context.source ?: return@async
                        if (sender !is Player) {
                            sender.sendLang("Messages.onlyPlayer")
                            return@async
                        }
                        val tip = context.getArgument<String>("get").equals("get", true)
                        val itemId = context.getArgument<String>("itemId")!!
                        val amount = context.getArgument<Int?>("amount")!!
                        giveCommand(sender, sender, itemId, amount, tip)
                    }
                }
            }
        }
    }

    @JvmStatic
    @CustomField(fieldType = "mm")
    val give = literal<CommandSender, Unit>("give", arrayListOf("give", "giveSilent")) {
        argument("player", PlayerArgument.NONNULL) {
            argument("itemId", MaybeMMItemIdArgument.INSTANCE) {
                argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                    setNullExecutor { context ->
                        async {
                            val sender = context.source ?: return@async
                            val tip = context.getArgument<String>("give").equals("give", true)
                            val player = context.getArgument<Player>("player")!!
                            val itemId = context.getArgument<String>("itemId")!!
                            val amount = context.getArgument<Int?>("amount")!!
                            giveCommand(sender, player, itemId, amount, tip)
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    @CustomField(fieldType = "mm")
    val giveAll = literal<CommandSender, Unit>("giveAll", arrayListOf("giveAll", "giveAllSilent")) {
        argument("itemId", MaybeMMItemIdArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                setNullExecutor { context ->
                    async {
                        val sender = context.source ?: return@async
                        if (sender !is Player) {
                            sender.sendLang("Messages.onlyPlayer")
                            return@async
                        }
                        val tip = context.getArgument<String>("giveAll").equals("giveAll", true)
                        val itemId = context.getArgument<String>("itemId")!!
                        val amount = context.getArgument<Int?>("amount")!!
                        Bukkit.getOnlinePlayers().forEach { player ->
                            giveCommand(
                                sender, player, itemId, amount, tip
                            )
                        }
                    }
                }
            }
        }
    }

    private fun giveCommand(
        sender: CommandSender,
        player: Player,
        itemId: String,
        amount: Int,
        tip: Boolean
    ) {
        val itemStack = mythicMobsHooker!!.getItemStack(itemId) ?: let {
            sender.sendLang("Messages.unknownItem", mapOf(Pair("{itemID}", itemId)))
            return
        }
        sync {
            player.giveItems(itemStack, amount.coerceAtLeast(1))
        }
        if (tip) {
            val name = itemStack.getParsedName()
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
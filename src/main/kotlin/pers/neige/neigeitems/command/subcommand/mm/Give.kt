package pers.neige.neigeitems.command.subcommand.mm

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.CommandUtils
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.positiveInteger
import pers.neige.neigeitems.command.arguments.MMItemIDArgumentType.getMMItemID
import pers.neige.neigeitems.command.arguments.MMItemIDArgumentType.mmItemID
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayerSelector
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.PlayerSelector
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
    private val getLogic: RequiredArgumentBuilder<CommandSender, String> =
        // ni mm get [item]
        argument<CommandSender, String>("item", mmItemID()).executes { context ->
            give(context)
        }.then(
            // ni mm get [item] (amount)
            argument<CommandSender, Int>("amount", positiveInteger()).executes { context ->
                give(context, getInteger(context, "amount"))
            }
        )

    private val giveLogic: RequiredArgumentBuilder<CommandSender, PlayerSelector> =
        // ni mm give [player]
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
        amount: Int = 1
    ): Int {
        async {
            val get = context.nodes[1].node.name.startsWith("get")
            val all = context.nodes[1].node.name.startsWith("giveAll")
            val tip = !context.nodes[1].node.name.endsWith("Silent")
            val sender = context.source
            if (get && sender !is Player) {
                sender.sendLang("Messages.onlyPlayer")
                return@async
            }
            val mmItemID = getMMItemID(context, "item")
            val itemStack = mythicMobsHooker!!.getItemStack(mmItemID) ?: let {
                sender.sendLang("Messages.unknownItem", mapOf(Pair("{itemID}", mmItemID)))
                return@async
            }
            if (all) {
                Bukkit.getOnlinePlayers().forEach { player ->
                    giveCommand(
                        sender, player, itemStack, amount, tip
                    )
                }
            } else {
                val player = if (get) {
                    sender as Player
                } else {
                    val playerSelector = getPlayerSelector(context, "player")
                    playerSelector.select(context) ?: let {
                        sender.sendLang("Messages.invalidPlayer", mapOf(Pair("{player}", playerSelector.text)))
                        return@async
                    }
                }
                giveCommand(
                    sender, player, itemStack, amount, tip
                )
            }
        }
        return 1
    }

    private fun giveCommand(
        sender: CommandSender,
        player: Player,
        itemStack: ItemStack,
        amount: Int,
        tip: Boolean
    ) {
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
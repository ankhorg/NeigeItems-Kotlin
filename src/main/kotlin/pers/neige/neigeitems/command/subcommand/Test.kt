package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.IntegerArgumentType
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.ItemArgumentType.getItemSelector
import pers.neige.neigeitems.command.arguments.ItemArgumentType.item
import pers.neige.neigeitems.command.selector.ItemSelector
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni test指令
 */
object Test {
    // ni test
    val test: LiteralArgumentBuilder<CommandSender> =
        literal<CommandSender>("test").then(
            // ni test [item]
            argument<CommandSender, ItemSelector>("item", item()).executes { context ->
                test(context)
            }.then(
                // ni test [item] (amount)
                argument<CommandSender, Int>("amount", IntegerArgumentType.positiveInteger()).executes { context ->
                    test(context, getInteger(context, "amount"))
                }
            )
        )

    fun test(
        context: CommandContext<CommandSender>,
        amount: Int = 1
    ): Int {
        async {
            val sender = context.source
            if (sender !is Player) {
                sender.sendLang("Messages.onlyPlayer")
                return@async
            }
            val itemSelector = getItemSelector(context, "item")
            val item = itemSelector.getItem(context) ?: let {
                sender.sendLang("Messages.unknownItem", mapOf(Pair("{itemID}", itemSelector.id)))
                return@async
            }
            val time = System.currentTimeMillis()
            repeat(amount) {
                item.getItemStack(sender, HashMap<String, String>())
            }
            sender.sendMessage("耗时: ${System.currentTimeMillis() - time}ms")
        }
        return 1
    }
}
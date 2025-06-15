package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.argument
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.IntegerArgument
import pers.neige.neigeitems.colonel.argument.command.ItemArgument
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni test指令
 */
object Test {
    @JvmStatic
    @CustomField(fieldType = "root")
    val test = literal<CommandSender, Unit>("test") {
        argument("item", ItemArgument.INSTANCE) {
            argument("amount", IntegerArgument.POSITIVE_DEFAULT_ONE) {
                setNullExecutor { context ->
                    async {
                        val sender = context.source ?: return@async
                        if (sender !is Player) {
                            sender.sendLang("Messages.onlyPlayer")
                            return@async
                        }
                        val itemContainer = context.getArgument<ItemArgument.ItemContainer>("item")
                        val item = itemContainer.itemGenerator ?: return@async
                        val amount = context.getArgument<Int?>("amount")!!
                        val time = System.currentTimeMillis()
                        repeat(amount) {
                            item.getItemStack(sender, HashMap())
                        }
                        sender.sendMessage("耗时: ${System.currentTimeMillis() - time}ms")
                    }
                }
            }
        }
    }
}
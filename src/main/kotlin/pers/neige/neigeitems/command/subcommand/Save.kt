package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.argument
import pers.neige.colonel.context.Context
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.FileNameArgument
import pers.neige.neigeitems.colonel.argument.command.MaybeItemIdArgument
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ConfigUtils.getDirectoryOrCreate
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni save指令
 */
object Save {
    @JvmStatic
    @CustomField(fieldType = "root")
    val save = literal<CommandSender, Unit>("save", arrayListOf("save", "cover")) {
        argument("item", MaybeItemIdArgument.INSTANCE) {
            setNullExecutor { context ->
                handle(context, context.getArgument("item"))
            }
            argument("path", FileNameArgument { getDirectoryOrCreate("Items") }) {
                setNullExecutor { context ->
                    handle(context, context.getArgument("item"), context.getArgument("path"))
                }
            }
        }
    }

    private fun handle(context: Context<CommandSender, Unit>, id: String, path: String = "$id.yml") {
        val sender = context.source ?: return
        if (sender !is Player) {
            sender.sendLang("Messages.onlyPlayer")
            return
        }
        val cover = context.getArgument<String>("save").equals("cover", true)
        val itemStack = sender.inventory.itemInMainHand
        async {
            when (ItemManager.saveItem(itemStack, id, path, cover)) {
                // 保存成功
                ItemManager.SaveResult.SUCCESS -> {
                    sender.sendLang(
                        "Messages.successSaveInfo", mapOf(
                            Pair("{name}", itemStack.getName()),
                            Pair("{itemID}", id),
                            Pair("{path}", path)
                        )
                    )
                }
                // 已存在对应ID物品
                ItemManager.SaveResult.CONFLICT -> {
                    if (cover) return@async
                    sender.sendLang("Messages.existedKey", mapOf(Pair("{itemID}", id)))
                }
                // 你保存了个空气
                ItemManager.SaveResult.AIR -> sender.sendLang("Messages.airItem")
            }
        }
    }
}
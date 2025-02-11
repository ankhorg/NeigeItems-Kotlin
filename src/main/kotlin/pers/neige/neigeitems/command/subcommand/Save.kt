package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.FileNameArgumentType.fileName
import pers.neige.neigeitems.command.arguments.FileNameArgumentType.getFileName
import pers.neige.neigeitems.command.arguments.ItemArgumentType.item
import pers.neige.neigeitems.command.arguments.UnquotedStringArgumentType.getUnquotedString
import pers.neige.neigeitems.command.arguments.UnquotedStringArgumentType.string
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni save指令
 */
object Save {
    private val saveLogic: RequiredArgumentBuilder<CommandSender, String> =
        // ni save [item]
        argument<CommandSender, String>("item", string()).executes { context ->
            save(context, getUnquotedString(context, "item"))
            1
        }.suggests { context, builder ->
            item().listSuggestions(context, builder)
        }.then(
            // ni save [item] (path)
            argument<CommandSender, String>("path", fileName("Items")).executes { context ->
                save(context, getUnquotedString(context, "item"), getFileName(context, "path"))
                1
            }
        )

    // ni save
    val save: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("save").then(saveLogic)

    // ni cover
    val cover: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("cover").then(saveLogic)

    private fun save(context: CommandContext<CommandSender>, id: String, path: String = "$id.yml") {
        val cover = context.nodes[0].node.name == "cover"
        val sender = context.source
        if (sender !is Player) {
            sender.sendLang("Messages.onlyPlayer")
            return
        }
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
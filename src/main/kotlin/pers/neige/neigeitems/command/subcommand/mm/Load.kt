package pers.neige.neigeitems.command.subcommand.mm

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.FileNameArgumentType.fileName
import pers.neige.neigeitems.command.arguments.FileNameArgumentType.getFileName
import pers.neige.neigeitems.command.arguments.UnquotedStringArgumentType.getUnquotedString
import pers.neige.neigeitems.command.arguments.UnquotedStringArgumentType.string
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrCreate
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import java.io.File

/**
 * ni mm load指令
 */
object Load {
    private val loadLogic: RequiredArgumentBuilder<CommandSender, String> =
        // ni mm load [item]
        argument<CommandSender, String>("item", string()).executes { context ->
            save(context, getUnquotedString(context, "item"))
            1
        }.suggests { _, builder ->
            mythicMobsHooker!!.getItemIds().forEach {
                builder.suggest(it)
            }
            builder.buildFuture()
        }.then(
            // ni mm load [item] (path)
            argument<CommandSender, String>("path", fileName("Items")).executes { context ->
                save(context, getUnquotedString(context, "item"), getFileName(context, "path"))
                1
            }
        )

    // ni mm load
    val load: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("load").then(loadLogic)

    // ni mm cover
    val cover: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("cover").then(loadLogic)

    val loadAll: LiteralArgumentBuilder<CommandSender> =
        // ni mm loadAll
        literal<CommandSender>("loadAll").executes { context ->
            val path = ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml"
            saveAll(context, path)
            1
        }.then(
            // ni mm loadAll (path)
            argument<CommandSender, String>("path", fileName("Items")).executes { context ->
                val path = getFileName(context, "path")
                saveAll(context, path)
                1
            }
        )

    private fun save(context: CommandContext<CommandSender>, id: String, path: String = "$id.yml") {
        val file = getFileOrCreate("Items${File.separator}$path")
        val config = YamlConfiguration.loadConfiguration(file)
        save(context, id, file, config)
    }

    private fun saveAll(context: CommandContext<CommandSender>, path: String) {
        val file = getFileOrCreate("Items${File.separator}$path")
        val config = YamlConfiguration.loadConfiguration(file)
        mythicMobsHooker!!.getItemIds().forEach {
            save(context, it, file, config)
        }
    }

    private fun save(context: CommandContext<CommandSender>, id: String, file: File, config: YamlConfiguration) {
        val cover = context.nodes[0].node.name == "cover"
        val sender = context.source
        val itemStack = try {
            mythicMobsHooker!!.getItemStackSync(id) ?: let {
                sender.sendLang(
                    "Messages.unknownItem", mapOf(
                        Pair("{itemID}", id)
                    )
                )
                return
            }
        } catch (error: Throwable) {
            sender.sendLang(
                "Messages.invalidMMItem", mapOf(
                    Pair("{itemID}", id),
                )
            )
            error.printStackTrace()
            return
        }
        async {
            when (ItemManager.saveItem(itemStack, id, file, config, cover)) {
                // 保存成功
                ItemManager.SaveResult.SUCCESS -> {
                    sender.sendLang(
                        "Messages.successSaveInfo", mapOf(
                            Pair("{name}", itemStack.getName()),
                            Pair("{itemID}", id),
                            Pair("{path}", file.path)
                        )
                    )
                }
                // 已存在对应ID物品
                ItemManager.SaveResult.CONFLICT -> {
                    if (cover) return@async
                    sender.sendLang(
                        "Messages.existedKey", mapOf(
                            Pair("{itemID}", id)
                        )
                    )
                }
                // 你保存了个空气
                ItemManager.SaveResult.AIR -> sender.sendLang("Messages.airItem")
            }
        }
    }
}
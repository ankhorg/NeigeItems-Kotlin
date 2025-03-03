package pers.neige.neigeitems.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContextBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.command.subcommand.*
import pers.neige.neigeitems.command.subcommand.Function
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.command.subcommand.List
import pers.neige.neigeitems.utils.CommandUtils
import pers.neige.neigeitems.utils.LangUtils.sendLang

/**
 * 插件指令
 */
object Command {
    const val COMMAND_NAME = "neigeitems"
    private var command: PluginCommand? = null
    private val dispatcher: CommandDispatcher<CommandSender> = CommandDispatcher()
    private val mapping: MutableMap<String, String> = mutableMapOf()

    private fun CommandDispatcher<CommandSender>.registerAndRecord(command: LiteralArgumentBuilder<CommandSender>): CommandDispatcher<CommandSender> {
        register(command)
        mapping[command.literal.lowercase()] = command.literal
        return this
    }

    private fun getPermission(context: CommandContextBuilder<*>): String {
        val permission = StringBuilder("$COMMAND_NAME.command")
        for (node in context.nodes) {
            if (node.node is LiteralCommandNode) {
                permission.append(".")
                permission.append(node.node.name.lowercase())
            } else {
                break
            }
        }
        return permission.toString()
    }

    @Awake
    fun init() {
        dispatcher
            .registerAndRecord(Action.action)
            .registerAndRecord(Drop.drop)
            .registerAndRecord(Drop.dropSilent)
            .registerAndRecord(DropPack.dropPack)
            .registerAndRecord(DropPack.dropPackSilent)
            .registerAndRecord(Editor.editHand)
            .registerAndRecord(Editor.editOffHand)
            .registerAndRecord(Editor.editSlot)
            .registerAndRecord(Expansion.expansion)
            .registerAndRecord(Function.function)
            .registerAndRecord(Give.get)
            .registerAndRecord(Give.getSilent)
            .registerAndRecord(Give.give)
            .registerAndRecord(Give.giveSilent)
            .registerAndRecord(Give.giveAll)
            .registerAndRecord(Give.giveAllSilent)
            .registerAndRecord(GivePack.givePack)
            .registerAndRecord(GivePack.givePackSilent)
            .registerAndRecord(help)
            .registerAndRecord(ItemNBT.itemNBT)
            .registerAndRecord(List.list)
            .registerAndRecord(MM.mm)
            .registerAndRecord(Parse.parse)
            .registerAndRecord(Reload.reload)
            .registerAndRecord(Save.save)
            .registerAndRecord(Save.cover)
            .registerAndRecord(Search.search)
            .registerAndRecord(Test.test)

        command = CommandUtils.newPluginCommand(COMMAND_NAME, NeigeItems.getInstance())?.apply {
            aliases = arrayListOf("ni")
            permission = "$COMMAND_NAME.command"
            setExecutor { sender, _, _, args ->
                val arg0 = args.getOrNull(0)?.lowercase()
                if (mapping.containsKey(arg0)) {
                    args[0] = mapping[arg0]
                }
                val parse: ParseResults<CommandSender> = dispatcher.parse(args.joinToString(" "), sender)
                if (parse.reader.canRead()) {
                    (parse.reader as StringReader).cursor = parse.reader.totalLength
                    if (parse.exceptions.isNotEmpty()) {
                        sender.sendMessage(parse.exceptions.values.first().rawMessage.string)
                        return@setExecutor true
                    }
                }
                if (parse.context.nodes.isEmpty()) {
                    help(sender)
                    return@setExecutor true
                }
                val permission = getPermission(parse.context)
                if (!sender.hasPermission(permission)) {
                    sender.sendLang(
                        "Messages.insufficientPermissions",
                        mapOf(Pair("{permission}", permission))
                    )
                    return@setExecutor true
                }
                return@setExecutor dispatcher.execute(parse) > 0
            }
            setTabCompleter { sender, _, _, args ->
                val lowercaseKey = args.getOrNull(0)?.lowercase()
                if (mapping.containsKey(lowercaseKey)) {
                    args[0] = mapping[lowercaseKey]
                }
                val parse: ParseResults<CommandSender> = dispatcher.parse(args.joinToString(" "), sender)
                val permission = getPermission(parse.context)
                if (!sender.hasPermission(permission)) {
                    return@setTabCompleter ArrayList()
                }
                return@setTabCompleter dispatcher.getCompletionSuggestions(parse).get().list.map { it.text }
            }
            CommandUtils.getCommandMap().register(this.name, this)
        }
    }
}
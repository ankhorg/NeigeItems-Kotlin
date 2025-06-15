package pers.neige.neigeitems.command

import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import pers.neige.colonel.CommandProcessor
import pers.neige.colonel.node.Node
import pers.neige.colonel.root
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.command.subcommand.Help
import pers.neige.neigeitems.utils.CommandUtils
import pers.neige.neigeitems.utils.LangUtils.sendLang

object Command {
    private val COMMAND_NAME = NeigeItems::class.java.simpleName.lowercase()
    private val ALIASES = arrayListOf(
        kotlin.text.StringBuilder().also {
            for (c in NeigeItems::class.java.simpleName) {
                if (c.isUpperCase() || c.isDigit()) it.append(c.lowercase())
            }
        }.toString()
    )
    private var command: PluginCommand? = null
    private val node = root<CommandSender, Unit>(COMMAND_NAME)

    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE)
    fun init() {
        NeigeItems.getInstance().scanner.getCustomFields("root", Node::class.java).forEach {
            Node.then(node, it as Node<CommandSender, Unit>, false)
        }
        node.buildLiteralSearcher()

        command = CommandUtils.newPluginCommand(COMMAND_NAME, NeigeItems.getInstance())?.apply {
            aliases = ALIASES
            permission = "$COMMAND_NAME.command"
            CommandProcessor.processCommand(this, node, { context, permission ->
                context.source!!.sendLang(
                    "Messages.insufficientPermissions",
                    mapOf(Pair("{permission}", permission))
                )
            }) { context ->
                Help.help(context.source!!)
            }
            CommandUtils.getCommandMap().register(this.name, this)
        }
    }
}

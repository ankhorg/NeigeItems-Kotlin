package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.arguments.impl.StringArgument
import pers.neige.colonel.node.impl.LiteralNode
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.PlayerArgument
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * ni parse指令
 */
object Parse {
    @JvmStatic
    @CustomField(fieldType = "root")
    val parse = LiteralNode.literal<CommandSender, Unit>("parse")
        .thenArgument("player", PlayerArgument.NULLABLE)
        .thenArgument("sections", StringArgument.builder<CommandSender, Unit>().readAll(true).build())
        .setNullExecutor { context ->
            val sender = context.source ?: return@setNullExecutor
            val player = context.getArgument<Player>("player")
            val sections = context.getArgument<String>("sections")
            sender.sendMessage(ActionManager.parseNode(sections, ActionContext(player)))
        }
}
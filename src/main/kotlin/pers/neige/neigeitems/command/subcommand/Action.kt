package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.command.subCommand

object Action {
    val action = subCommand {
        execute<CommandSender> { sender, _, _ ->
            async {
                help(sender)
            }
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                arrayListOf("me").also { list ->
                    Bukkit.getOnlinePlayers().forEach { player ->
                        list.add(player.name)
                    }
                }
            }
            execute<CommandSender> { sender, _, _ ->
                async {
                    help(sender)
                }
            }
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    ActionManager.actions.keys.toList().sorted()
                }
                execute<CommandSender> { sender, context, argument ->
                    var player = Bukkit.getPlayerExact(context.argument(-1))
                    if (player == null && sender is Player && context.argument(-1) == "me") {
                        player = sender
                    }
                    if (player != null) {
                        ActionManager.runAction(argument.parseSection(player), ActionContext(player))
                    } else {
                        ActionManager.runAction(argument.parseSection())
                    }
                }
            }
        }
    }
}
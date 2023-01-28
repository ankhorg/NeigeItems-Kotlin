package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

object Action {
    val action = subCommand {
        execute<CommandSender> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<CommandSender> { sender, _, _ ->
                submit(async = true) {
                    help(sender)
                }
            }
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    ActionManager.actions.keys.toList().sorted()
                }
                execute<CommandSender> { _, context, argument ->
                    submit(async = true) {
                        Bukkit.getPlayerExact(context.argument(-1))?.let { player ->
                            ActionManager.runAction(player, argument.parseSection(player))
                        }
                    }
                }
            }
        }
    }
}
package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.command.subCommand

object Parse {
    val parse = subCommand {
        execute<CommandSender> { sender, _, _ ->
            async {
                Help.help(sender)
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
                    Help.help(sender)
                }
            }
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("<>")
                }
                execute<CommandSender> { sender, context, argument ->
                    async {
                        var player = Bukkit.getPlayerExact(context.argument(-1))
                        if (player == null && sender is Player && context.argument(-1) == "me") {
                            player = sender
                        }
                        sender.sendMessage(argument.parseSection(player))
                    }
                }
            }
        }
    }
}
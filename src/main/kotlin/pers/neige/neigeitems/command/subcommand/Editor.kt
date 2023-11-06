package pers.neige.neigeitems.command.subcommand

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ItemEditorManager
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand

object Editor {
    val edithand = subCommand {
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
                    ItemEditorManager.editorNames.sorted()
                }
                execute<CommandSender> { sender, _, _ ->
                    async {
                        help(sender)
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("content")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        var player = Bukkit.getPlayerExact(context.argument(-2))
                        if (player == null && sender is Player && context.argument(-2) == "me") {
                            player = sender
                        }
                        player?.let { player ->
                            ItemEditorManager.runEditor(
                                context.argument(-1),
                                argument,
                                player.inventory.itemInMainHand,
                                player
                            )
                        }
                    }
                }
            }
        }
    }

    val editoffhand = subCommand {
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
                    ItemEditorManager.editorNames.sorted()
                }
                execute<CommandSender> { sender, _, _ ->
                    async {
                        help(sender)
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("content")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        var player = Bukkit.getPlayerExact(context.argument(-2))
                        if (player == null && sender is Player && context.argument(-2) == "me") {
                            player = sender
                        }
                        player?.let {
                            ItemEditorManager.runEditor(
                                context.argument(-1),
                                argument,
                                player.inventory.itemInOffHand,
                                player
                            )
                        }
                    }
                }
            }
        }
    }

    val editslot = subCommand {
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
                    arrayListOf("slot")
                }
                execute<CommandSender> { sender, _, _ ->
                    async {
                        help(sender)
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        ItemEditorManager.editorNames.sorted()
                    }
                    execute<CommandSender> { sender, _, _ ->
                        async {
                            help(sender)
                        }
                    }
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("content")
                        }
                        execute<CommandSender> { sender, context, argument ->
                            var player = Bukkit.getPlayerExact(context.argument(-3))
                            if (player == null && sender is Player && context.argument(-3) == "me") {
                                player = sender
                            }
                            player?.let {
                                player.inventory.getItem(context.argument(-2).toIntOrNull() ?: 0)?.let { itemStack ->
                                    ItemEditorManager.runEditor(context.argument(-1), argument, itemStack, player)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
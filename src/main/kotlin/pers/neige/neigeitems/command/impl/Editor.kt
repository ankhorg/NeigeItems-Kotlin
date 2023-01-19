package pers.neige.neigeitems.command.impl

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.command.impl.Help.help
import pers.neige.neigeitems.manager.ItemEditorManager
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

object Editor {
    val edithand = subCommand {
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
                    ItemEditorManager.editorNames.sorted()
                }
                execute<CommandSender> { sender, _, _ ->
                    submit(async = true) {
                        help(sender)
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("content")
                    }
                    execute<CommandSender> { _, context, argument ->
                        Bukkit.getPlayerExact(context.argument(-2))?.let { player ->
                            ItemEditorManager.runEditor(context.argument(-1), player, player.inventory.itemInMainHand, argument)
                        }
                    }
                }
            }
        }
    }

    val editoffhand = subCommand {
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
                    ItemEditorManager.editorNames.sorted()
                }
                execute<CommandSender> { sender, _, _ ->
                    submit(async = true) {
                        help(sender)
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("content")
                    }
                    execute<CommandSender> { _, context, argument ->
                        Bukkit.getPlayerExact(context.argument(-2))?.let { player ->
                            ItemEditorManager.runEditor(context.argument(-1), player, player.inventory.itemInOffHand, argument)
                        }
                    }
                }
            }
        }
    }

    val editslot = subCommand {
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
                    arrayListOf("slot")
                }
                execute<CommandSender> { sender, _, _ ->
                    submit(async = true) {
                        help(sender)
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        ItemEditorManager.editorNames.sorted()
                    }
                    execute<CommandSender> { sender, _, _ ->
                        submit(async = true) {
                            help(sender)
                        }
                    }
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("content")
                        }
                        execute<CommandSender> { _, context, argument ->
                            Bukkit.getPlayerExact(context.argument(-3))?.let { player ->
                                player.inventory.getItem(context.argument(-2).toIntOrNull() ?: 0)?.let { itemStack ->
                                    ItemEditorManager.runEditor(context.argument(-1), player, itemStack, argument)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
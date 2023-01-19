package pers.neige.neigeitems.command.impl

import org.bukkit.entity.Player
import pers.neige.neigeitems.command.impl.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ItemManager
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName
import java.io.File

object Save {
    val save = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni save [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                arrayListOf("id")
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    when (ItemManager.saveItem(sender.inventory.itemInMainHand, argument, "$argument.yml", false)) {
                        // 保存成功
                        1 -> {
                            sender.sendMessage(
                                ConfigManager.config.getString("Messages.successSaveInfo")
                                ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                ?.replace("{itemID}", argument)
                                ?.replace("{path}", "$argument.yml"))
                        }
                        // 已存在对应ID物品
                        0 -> sender.sendMessage(ConfigManager.config.getString("Messages.existedKey")?.replace("{itemID}", argument))
                        // 你保存了个空气
                        else -> sender.sendMessage(ConfigManager.config.getString("Messages.airItem"))
                    }
                }
            }
            // ni save [物品ID] (保存路径)
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                }
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        when (ItemManager.saveItem(sender.inventory.itemInMainHand, context.argument(-1), argument, false)) {
                            // 保存成功
                            1 -> {
                                sender.sendMessage(
                                    ConfigManager.config.getString("Messages.successSaveInfo")
                                    ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                    ?.replace("{itemID}", context.argument(-1))
                                    ?.replace("{path}", argument))
                            }
                            // 已存在对应ID物品
                            0 -> sender.sendMessage(ConfigManager.config.getString("Messages.existedKey")?.replace("{itemID}", context.argument(-1)))
                            // 你保存了个空气
                            else -> sender.sendMessage(ConfigManager.config.getString("Messages.airItem"))
                        }
                    }
                }
            }
        }
    }

    // ni cover [物品ID] (保存路径) > 将手中物品以对应ID覆盖至对应路径
    val cover = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni cover [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                arrayListOf("id")
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    when (ItemManager.saveItem(sender.inventory.itemInMainHand, argument, "$argument.yml", true)) {
                        // 你保存了个空气
                        2 -> sender.sendMessage(ConfigManager.config.getString("Messages.airItem"))
                        // 保存成功
                        else -> {
                            sender.sendMessage(
                                ConfigManager.config.getString("Messages.successSaveInfo")
                                ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                ?.replace("{itemID}", argument)
                                ?.replace("{path}", "$argument.yml"))
                        }
                    }
                }
            }
            // ni cover [物品ID] (保存路径)
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                }
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        when (ItemManager.saveItem(sender.inventory.itemInMainHand, context.argument(-1), argument, true)) {
                            // 你保存了个空气
                            2 -> sender.sendMessage(ConfigManager.config.getString("Messages.airItem"))
                            // 保存成功
                            else -> {
                                sender.sendMessage(
                                    ConfigManager.config.getString("Messages.successSaveInfo")
                                    ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                    ?.replace("{itemID}", context.argument(-1))
                                    ?.replace("{path}", argument))
                            }
                        }
                    }
                }
            }
        }
    }
}
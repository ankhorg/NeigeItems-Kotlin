package pers.neige.neigeitems.command.impl

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.command.impl.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName
import java.io.File

object MM {
    val mm = subCommand {
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                arrayListOf("load", "cover", "loadAll", "get", "give", "giveAll")
            }
            execute<CommandSender> { sender, _, argument ->
                submit(async = true) {
                    HookerManager.mythicMobsHooker?.let {
                        when (argument) {
                            // ni mm loadAll
                            "loadAll" -> {
                                HookerManager.mythicMobsHooker!!.getItemIds().forEach { id ->
                                    HookerManager.mythicMobsHooker!!.getItemStackSync(id)?.let { itemStack ->
                                        when (ItemManager.saveItem(itemStack, id, ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml", false)) {
                                            // 保存成功
                                            1 -> {
                                                sender.sendMessage(
                                                    ConfigManager.config.getString("Messages.successSaveInfo")
                                                    ?.replace("{name}", itemStack.getName())
                                                    ?.replace("{itemID}", id)
                                                    ?.replace("{path}", ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml"))
                                            }
                                            // 已存在对应ID物品
                                            0 -> sender.sendMessage(ConfigManager.config.getString("Messages.existedKey")?.replace("{itemID}", id))
                                        }
                                    }
                                }
                            }
                            else -> help(sender)
                        }
                    } ?: sender.sendMessage(ConfigManager.config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                }
            }
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, context ->
                    HookerManager.mythicMobsHooker?.let {
                        when (context.argument(-1)) {
                            // ni mm load [物品ID]
                            "load" -> HookerManager.mythicMobsHooker!!.getItemIds()
                            // ni mm cover [物品ID]
                            "cover" -> HookerManager.mythicMobsHooker!!.getItemIds()
                            // ni mm loadAll (保存路径)
                            "loadAll" -> ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                            // ni mm get [物品ID]
                            "get" -> HookerManager.mythicMobsHooker!!.getItemIds()
                            // ni mm give [玩家ID]
                            "give" -> Bukkit.getOnlinePlayers().map { it.name }
                            // ni mm giveAll [物品ID]
                            "giveAll" -> HookerManager.mythicMobsHooker!!.getItemIds()
                            else -> arrayListOf()
                        }
                    } ?: arrayListOf()
                }
                execute<CommandSender> { sender, context, argument ->
                    submit(async = true) {
                        HookerManager.mythicMobsHooker?.let {
                            when (context.argument(-1)) {
                                // ni mm load [物品ID]
                                "load" -> {
                                    HookerManager.mythicMobsHooker!!.getItemStackSync(argument)?.let { itemStack ->
                                        when (ItemManager.saveItem(itemStack, argument, "$argument.yml", false)) {
                                            // 保存成功
                                            1 -> {
                                                sender.sendMessage(
                                                    ConfigManager.config.getString("Messages.successSaveInfo")
                                                    ?.replace("{name}", itemStack.getName())
                                                    ?.replace("{itemID}", argument)
                                                    ?.replace("{path}", "$argument.yml"))
                                            }
                                            // 已存在对应ID物品
                                            0 -> sender.sendMessage(ConfigManager.config.getString("Messages.existedKey")?.replace("{itemID}", argument))
                                        }
                                        // 未知物品
                                    } ?: let {
                                        sender.sendMessage(ConfigManager.config.getString("Messages.unknownItem")?.replace("{itemID}", argument))
                                    }
                                }
                                // ni mm cover [物品ID]
                                "cover" -> {
                                    HookerManager.mythicMobsHooker!!.getItemStackSync(argument)?.let { itemStack ->
                                        if (ItemManager.saveItem(itemStack, argument, "$argument.yml", true) != 2) {
                                            // 保存成功
                                            sender.sendMessage(
                                                ConfigManager.config.getString("Messages.successSaveInfo")
                                                ?.replace("{name}", itemStack.getName())
                                                ?.replace("{itemID}", argument)
                                                ?.replace("{path}", "$argument.yml"))
                                        }
                                        // 未知物品
                                    } ?: let {
                                        sender.sendMessage(ConfigManager.config.getString("Messages.unknownItem")?.replace("{itemID}", argument))
                                    }
                                }
                                // ni mm loadAll (保存路径)
                                "loadAll" -> {
                                    HookerManager.mythicMobsHooker!!.getItemIds().forEach { id ->
                                        HookerManager.mythicMobsHooker!!.getItemStackSync(id)?.let { itemStack ->
                                            when (ItemManager.saveItem(itemStack, id, argument, false)) {
                                                // 保存成功
                                                1 -> {
                                                    sender.sendMessage(
                                                        ConfigManager.config.getString("Messages.successSaveInfo")
                                                        ?.replace("{name}", itemStack.getName())
                                                        ?.replace("{itemID}", id)
                                                        ?.replace("{path}", argument))
                                                }
                                                // 已存在对应ID物品
                                                0 -> sender.sendMessage(ConfigManager.config.getString("Messages.existedKey")?.replace("{itemID}", id))
                                            }
                                        }
                                    }
                                }
                                // ni mm get [物品ID]
                                "get" -> {
                                    if (sender is Player) {
                                        giveAddonCommandAsync( sender, sender, argument, HookerManager.mythicMobsHooker!!.getItemStackSync(argument), 1)
                                    } else {
                                        ConfigManager.config.getString("Messages.onlyPlayer")?.let { sender.sendMessage(it) }
                                    }
                                }
                                // ni mm giveAll [物品ID]
                                "giveAll" -> {
                                    submit(async = true) {
                                        Bukkit.getOnlinePlayers().forEach { player ->
                                            giveAddonCommand( sender, player, argument, HookerManager.mythicMobsHooker!!.getItemStackSync(argument), 1)
                                        }
                                    }
                                }
                                else -> help(sender)
                            }
                        } ?: sender.sendMessage(ConfigManager.config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                    }
                }
                dynamic {
                    suggestion<CommandSender>(uncheck = true) { _, context ->
                        HookerManager.mythicMobsHooker?.let {
                            when (context.argument(-2)) {
                                // ni mm load [物品ID] (保存路径)
                                "load" -> ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                                // ni mm cover [物品ID] (保存路径)
                                "cover" -> ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                                // ni mm get [物品ID] (数量)
                                "get" -> arrayListOf("amount")
                                // ni mm give [玩家ID] [物品ID]
                                "give" -> HookerManager.mythicMobsHooker!!.getItemIds()
                                // ni mm giveAll [物品ID] (数量)
                                "giveAll" -> arrayListOf("amount")
                                else -> arrayListOf()
                            }
                        } ?: arrayListOf()
                    }
                    execute<CommandSender> { sender, context, argument ->
                        submit(async = true) {
                            HookerManager.mythicMobsHooker?.let {
                                when (context.argument(-2)) {
                                    // ni mm load [物品ID] (保存路径)
                                    "load" -> {
                                        HookerManager.mythicMobsHooker!!.getItemStackSync(context.argument(-1))?.let { itemStack ->
                                            when (ItemManager.saveItem(itemStack, context.argument(-1), argument, false)) {
                                                // 保存成功
                                                1 -> {
                                                    sender.sendMessage(
                                                        ConfigManager.config.getString("Messages.successSaveInfo")
                                                        ?.replace("{name}", itemStack.getName())
                                                        ?.replace("{itemID}", context.argument(-1))
                                                        ?.replace("{path}", argument))
                                                }
                                                // 已存在对应ID物品
                                                0 -> sender.sendMessage(ConfigManager.config.getString("Messages.existedKey")?.replace("{itemID}", context.argument(-1)))
                                            }
                                            // 未知物品
                                        } ?: let {
                                            sender.sendMessage(ConfigManager.config.getString("Messages.unknownItem")?.replace("{itemID}", context.argument(-1)))
                                        }
                                    }
                                    // ni mm cover [物品ID] (保存路径)
                                    "cover" -> {
                                        HookerManager.mythicMobsHooker!!.getItemStackSync(context.argument(-1))?.let { itemStack ->
                                            if (ItemManager.saveItem(itemStack, context.argument(-1), argument, true) != 2) {
                                                // 保存成功
                                                sender.sendMessage(
                                                    ConfigManager.config.getString("Messages.successSaveInfo")
                                                    ?.replace("{name}", itemStack.getName())
                                                    ?.replace("{itemID}", context.argument(-1))
                                                    ?.replace("{path}", argument))
                                            }
                                            // 未知物品
                                        } ?: let {
                                            sender.sendMessage(ConfigManager.config.getString("Messages.unknownItem")?.replace("{itemID}", context.argument(-1)))
                                        }
                                    }
                                    // ni mm get [物品ID] (数量)
                                    "get" -> {
                                        if (sender is Player) {
                                            giveAddonCommandAsync( sender, sender, context.argument(-1), HookerManager.mythicMobsHooker!!.getItemStackSync(context.argument(-1)), argument.toIntOrNull())
                                        } else {
                                            ConfigManager.config.getString("Messages.onlyPlayer")?.let { sender.sendMessage(it) }
                                        }
                                    }
                                    // ni mm give [玩家ID] [物品ID]
                                    "give" -> {
                                        giveAddonCommandAsync( sender, Bukkit.getPlayerExact(context.argument(-1)), argument, HookerManager.mythicMobsHooker!!.getItemStackSync(argument), 1)
                                    }
                                    // ni mm giveAll [物品ID] (数量)
                                    "giveAll" -> {
                                        submit(async = true) {
                                            Bukkit.getOnlinePlayers().forEach { player ->
                                                giveAddonCommand( sender, player, context.argument(-1), HookerManager.mythicMobsHooker!!.getItemStackSync(context.argument(-1)), argument.toIntOrNull())
                                            }
                                        }
                                    }
                                    else -> help(sender)
                                }
                            } ?: sender.sendMessage(ConfigManager.config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                        }
                    }
                    dynamic {
                        suggestion<CommandSender>(uncheck = true) { _, context ->
                            HookerManager.mythicMobsHooker?.let {
                                when (context.argument(-3)) {
                                    // ni mm give [玩家ID] [物品ID] (数量)
                                    "give" -> HookerManager.mythicMobsHooker!!.getItemIds()
                                    else -> arrayListOf()
                                }
                            } ?: arrayListOf()
                        }
                        execute<CommandSender> { sender, context, argument ->
                            submit(async = true) {
                                HookerManager.mythicMobsHooker?.let {
                                    when (context.argument(-3)) {
                                        // ni mm give [玩家ID] [物品ID] (数量)
                                        "give" -> {
                                            giveAddonCommandAsync( sender, Bukkit.getPlayerExact(context.argument(-2)), context.argument(-1), HookerManager.mythicMobsHooker!!.getItemStackSync(context.argument(-1)), argument.toIntOrNull())
                                        }
                                        else -> help(sender)
                                    }
                                } ?: sender.sendMessage(ConfigManager.config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                            }
                        }
                    }
                }
            }
        }
    }
    
    private fun giveAddonCommandAsync(
        sender: CommandSender,
        player: Player?,
        id: String,
        itemStack: ItemStack?,
        amount: Int?
    ) {
        submit (async = true) {
            giveAddonCommand(sender, player, id, itemStack, amount)
        }
    }

    private fun giveAddonCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        itemStack: ItemStack?,
        amount: Int?
    ) {
        player?.let {
            // 获取数量
            amount?.let {
                // 给物品
                itemStack?.let {
                    NeigeItems.bukkitScheduler.callSyncMethod(NeigeItems.plugin) {
                        player.giveItems(itemStack, amount.coerceAtLeast(1))
                    }
                    sender.sendMessage(
                        ConfigManager.config.getString("Messages.successInfo")
                        ?.replace("{player}", player.name)
                        ?.replace("{amount}", amount.toString())
                        ?.replace("{name}", itemStack.getName()))
                    player.sendMessage(
                        ConfigManager.config.getString("Messages.givenInfo")
                        ?.replace("{amount}", amount.toString())
                        ?.replace("{name}", itemStack.getName()))
                    // 未知物品ID
                } ?: let {
                    sender.sendMessage(ConfigManager.config.getString("Messages.unknownItem")?.replace("{itemID}", id))
                }
                // 无效数字
            } ?: let {
                sender.sendMessage(ConfigManager.config.getString("Messages.invalidAmount"))
            }
            // 无效玩家
        } ?: let {
            sender.sendMessage(ConfigManager.config.getString("Messages.invalidPlayer"))
        }
    }
}
package pers.neige.neigeitems.command.subcommand

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand
import java.io.File

object MMSave {
    // ni mm load [物品ID] (保存路径) > 将对应ID的MM物品保存为NI物品
    val load = subCommand {
        execute<Player> { sender, _, _ ->
            async {
                help(sender)
            }
        }
        // ni mm load [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                mythicMobsHooker!!.getItemIds()
            }
            execute<Player> { sender, _, argument ->
                async {
                    try {
                        mythicMobsHooker!!.getItemStackSync(argument)?.let { itemStack ->
                            when (ItemManager.saveItem(itemStack, argument, "$argument.yml", false)) {
                                // 保存成功
                                1 -> {
                                    sender.sendLang(
                                        "Messages.successSaveInfo", mapOf(
                                            Pair("{name}", itemStack.getName()),
                                            Pair("{itemID}", argument),
                                            Pair("{path}", "$argument.yml")
                                        )
                                    )
                                }
                                // 已存在对应ID物品
                                0 -> {
                                    sender.sendLang(
                                        "Messages.existedKey", mapOf(
                                            Pair("{itemID}", argument)
                                        )
                                    )
                                }
                            }
                            // 未知物品
                        } ?: let {
                            sender.sendLang(
                                "Messages.unknownItem", mapOf(
                                    Pair("{itemID}", argument)
                                )
                            )
                        }
                    } catch (error: Throwable) {
                        sender.sendLang(
                            "Messages.invalidMMItem", mapOf(
                                Pair("{itemID}", argument),
                            )
                        )
                        error.printStackTrace()
                    }
                }
            }
            // ni mm load [物品ID] (保存路径)
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map {
                        it.path.replace(
                            "plugins${File.separator}NeigeItems${File.separator}Items${File.separator}",
                            ""
                        )
                    }
                }
                execute<Player> { sender, context, argument ->
                    async {
                        try {
                            mythicMobsHooker!!.getItemStackSync(context.argument(-1))?.let { itemStack ->
                                when (ItemManager.saveItem(itemStack, context.argument(-1), argument, false)) {
                                    // 保存成功
                                    1 -> {
                                        sender.sendLang(
                                            "Messages.successSaveInfo", mapOf(
                                                Pair("{name}", itemStack.getName()),
                                                Pair("{itemID}", context.argument(-1)),
                                                Pair("{path}", argument)
                                            )
                                        )
                                    }
                                    // 已存在对应ID物品
                                    0 -> {
                                        sender.sendLang(
                                            "Messages.existedKey", mapOf(
                                                Pair("{itemID}", context.argument(-1))
                                            )
                                        )
                                    }
                                }
                                // 未知物品
                            } ?: let {
                                sender.sendLang(
                                    "Messages.unknownItem", mapOf(
                                        Pair("{itemID}", context.argument(-1))
                                    )
                                )
                            }
                        } catch (error: Throwable) {
                            sender.sendLang(
                                "Messages.invalidMMItem", mapOf(
                                    Pair("{itemID}", context.argument(-1)),
                                )
                            )
                            error.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    // ni mm cover [物品ID] (保存路径) > 将对应ID的MM物品覆盖为NI物品
    val cover = subCommand {
        execute<Player> { sender, _, _ ->
            async {
                help(sender)
            }
        }
        // ni mm cover [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                mythicMobsHooker!!.getItemIds()
            }
            execute<Player> { sender, _, argument ->
                async {
                    try {
                        mythicMobsHooker!!.getItemStackSync(argument)?.let { itemStack ->
                            if (ItemManager.saveItem(itemStack, argument, "$argument.yml", true) != 2) {
                                // 保存成功
                                sender.sendLang(
                                    "Messages.successSaveInfo", mapOf(
                                        Pair("{name}", itemStack.getName()),
                                        Pair("{itemID}", argument),
                                        Pair("{path}", "$argument.yml")
                                    )
                                )
                            }
                            // 未知物品
                        } ?: let {
                            sender.sendLang(
                                "Messages.unknownItem", mapOf(
                                    Pair("{itemID}", argument)
                                )
                            )
                        }
                    } catch (error: Throwable) {
                        sender.sendLang(
                            "Messages.invalidMMItem", mapOf(
                                Pair("{itemID}", argument),
                            )
                        )
                        error.printStackTrace()
                    }
                }
            }
            // ni cover [物品ID] (保存路径)
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map {
                        it.path.replace(
                            "plugins${File.separator}NeigeItems${File.separator}Items${File.separator}",
                            ""
                        )
                    }
                }
                execute<Player> { sender, context, argument ->
                    async {
                        try {
                            mythicMobsHooker!!.getItemStackSync(context.argument(-1))?.let { itemStack ->
                                if (ItemManager.saveItem(itemStack, context.argument(-1), argument, true) != 2) {
                                    // 保存成功
                                    sender.sendLang(
                                        "Messages.successSaveInfo", mapOf(
                                            Pair("{name}", itemStack.getName()),
                                            Pair("{itemID}", context.argument(-1)),
                                            Pair("{path}", argument),
                                        )
                                    )
                                }
                                // 未知物品
                            } ?: let {
                                sender.sendLang(
                                    "Messages.unknownItem", mapOf(
                                        Pair("{itemID}", context.argument(-1))
                                    )
                                )
                            }
                        } catch (error: Throwable) {
                            sender.sendLang(
                                "Messages.invalidMMItem", mapOf(
                                    Pair("{itemID}", context.argument(-1)),
                                )
                            )
                            error.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    // ni mm loadAll (保存路径) > 将全部MM物品转化为NI物品
    val loadAll = subCommand {
        // ni mm loadAll
        execute<Player> { sender, _, _ ->
            async {
                val path = ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml"
                val file = File(NeigeItems.plugin.dataFolder, "${File.separator}Items${File.separator}$path")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val config = YamlConfiguration.loadConfiguration(file)
                mythicMobsHooker!!.getItemIds().forEach { id ->
                    try {
                        mythicMobsHooker!!.getItemStackSync(id)?.let { itemStack ->
                            when (ItemManager.saveItem(itemStack, id, file, config, false)) {
                                // 保存成功
                                1 -> {
                                    sender.sendLang(
                                        "Messages.successSaveInfo", mapOf(
                                            Pair("{name}", itemStack.getName()),
                                            Pair("{itemID}", id),
                                            Pair(
                                                "{path}",
                                                ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml"
                                            )
                                        )
                                    )
                                }
                                // 已存在对应ID物品
                                0 -> {
                                    sender.sendLang(
                                        "Messages.existedKey", mapOf(
                                            Pair("{itemID}", id)
                                        )
                                    )
                                }
                            }
                        }
                    } catch (error: Throwable) {
                        sender.sendLang(
                            "Messages.invalidMMItem", mapOf(
                                Pair("{itemID}", id),
                            )
                        )
                        error.printStackTrace()
                    }
                }
            }
        }
        // ni mm loadAll (保存路径)
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.files.map {
                    it.path.replace(
                        "plugins${File.separator}NeigeItems${File.separator}Items${File.separator}",
                        ""
                    )
                }
            }
            execute<Player> { sender, _, argument ->
                async {
                    val file = File(NeigeItems.plugin.dataFolder, "${File.separator}Items${File.separator}$argument")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    val config = YamlConfiguration.loadConfiguration(file)
                    mythicMobsHooker!!.getItemIds().forEach { id ->
                        mythicMobsHooker!!.getItemStackSync(id)?.let { itemStack ->
                            when (ItemManager.saveItem(itemStack, id, file, config, false)) {
                                // 保存成功
                                1 -> {
                                    sender.sendLang(
                                        "Messages.successSaveInfo", mapOf(
                                            Pair("{name}", itemStack.getName()),
                                            Pair("{itemID}", id),
                                            Pair("{path}", argument)
                                        )
                                    )
                                }
                                // 已存在对应ID物品
                                0 -> {
                                    sender.sendLang(
                                        "Messages.existedKey", mapOf(
                                            Pair("{itemID}", id)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
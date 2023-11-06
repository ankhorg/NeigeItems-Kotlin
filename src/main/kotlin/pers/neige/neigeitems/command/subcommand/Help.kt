package pers.neige.neigeitems.command.subcommand

import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager.append
import pers.neige.neigeitems.manager.HookerManager.hoverText
import pers.neige.neigeitems.manager.HookerManager.runCommand
import pers.neige.neigeitems.utils.PlayerUtils.sendMessage
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import kotlin.math.ceil

object Help {
    val commandsPages by lazy {
        ConfigManager.config.getConfigurationSection("Help.commands")?.getKeys(false)?.size ?: 1
    }

    val helpCommand = subCommand {
        execute<CommandSender> { sender, _, _ ->
            help(sender)
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                (1..commandsPages).toList().map { it.toString() }
            }
            execute<CommandSender> { sender, _, argument ->
                help(sender, argument.toIntOrNull() ?: 1)
            }
        }
    }

    fun help(
        sender: CommandSender,
        page: Int = 1
    ) {
        // 获取帮助信息中的指令信息部分
        ConfigManager.config.getConfigurationSection("Help.commands")?.let { commandsSection ->
            // 获取所有指令
            val commands = commandsSection.getKeys(false).toMutableList()
            // 获取每一页展示几条指令
            val amount = ConfigManager.config.getInt("Help.amount")
            // 获取总页数
            val pageAmount = ceil(commands.size.toDouble() / amount.toDouble()).toInt()
            // 确定当前需要打开的页数
            val realPage = page.coerceAtMost(pageAmount).coerceAtLeast(1)
            // 发送前缀
            ConfigManager.config.getString("Help.prefix")?.let { sender.sendMessage(it) }
            // 获取指令帮助格式
            val format = ConfigManager.config.getString("Help.format") ?: ""
            // 获取当前序号
            val prevCommandAmount = ((realPage - 1) * amount)
            // 遍历指令并发送
            for (index in prevCommandAmount..(prevCommandAmount + amount)) {
                if (index == commands.size) break
                val command = commands[index]
                // 替换信息内变量并发送
                sender.sendMessage(
                    format
                        .replace("{command}", commandsSection.getString("$command.command") ?: "")
                        .replace("{description}", commandsSection.getString("$command.description") ?: "")
                )
            }
            val prevRaw = ComponentBuilder(ConfigManager.config.getString("Help.prev") ?: "")
            if (realPage != 1) {
                prevRaw
                    .hoverText((ConfigManager.config.getString("Help.prev") ?: "") + ": " + (realPage - 1).toString())
                    .runCommand("/ni help ${realPage - 1}")
            }
            val nextRaw = ComponentBuilder(ConfigManager.config.getString("Help.next") ?: "")
            if (realPage != pageAmount) {
                nextRaw.hoverText((ConfigManager.config.getString("Help.next") ?: "") + ": " + (realPage + 1))
                nextRaw.runCommand("/ni help ${realPage + 1}")
            }
            var listSuffixMessage = (ConfigManager.config.getString("Help.suffix") ?: "")
                .replace("{current}", realPage.toString())
                .replace("{total}", pageAmount.toString())
            val listMessage = ComponentBuilder("")
            if (sender is Player) {
                listSuffixMessage = listSuffixMessage
                    .replace("{prev}", "!@#$%{prev}!@#$%")
                    .replace("{next}", "!@#$%{next}!@#$%")
                val listSuffixMessageList = listSuffixMessage.split("!@#$%")
                listSuffixMessageList.forEach { value ->
                    when (value) {
                        "{prev}" -> listMessage.append(prevRaw)
                        "{next}" -> listMessage.append(nextRaw)
                        else -> listMessage.append(value)
                    }
                }
                // 向玩家发送信息
                sender.sendMessage(listMessage)
            } else {
                sender.sendMessage(
                    listSuffixMessage
                        .replace("{prev}", ConfigManager.config.getString("ItemList.Prev") ?: "")
                        .replace("{next}", ConfigManager.config.getString("ItemList.Next") ?: "")
                )
            }
        }
    }

    fun help(
        sender: ProxyCommandSender,
        page: Int = 1
    ) {
        // 获取帮助信息中的指令信息部分
        ConfigManager.config.getConfigurationSection("Help.commands")?.let { commandsSection ->
            // 获取所有指令
            val commands = commandsSection.getKeys(false).toMutableList()
            // 获取每一页展示几条指令
            val amount = ConfigManager.config.getInt("Help.amount")
            // 获取总页数
            val pageAmount = ceil(commands.size.toDouble() / amount.toDouble()).toInt()
            // 确定当前需要打开的页数
            val realPage = page.coerceAtMost(pageAmount).coerceAtLeast(1)
            // 发送前缀
            ConfigManager.config.getString("Help.prefix")?.let { sender.sendMessage(it) }
            // 获取指令帮助格式
            val format = ConfigManager.config.getString("Help.format") ?: ""
            // 获取当前序号
            val prevCommandAmount = ((realPage - 1) * amount)
            // 遍历指令并发送
            for (index in prevCommandAmount..(prevCommandAmount + amount)) {
                if (index == commands.size) break
                val command = commands[index]
                // 替换信息内变量并发送
                sender.sendMessage(
                    format
                        .replace("{command}", commandsSection.getString("$command.command") ?: "")
                        .replace("{description}", commandsSection.getString("$command.description") ?: "")
                )
            }
            val prevRaw = ComponentBuilder(ConfigManager.config.getString("Help.prev") ?: "")
            if (realPage != 1) {
                prevRaw
                    .hoverText((ConfigManager.config.getString("Help.prev") ?: "") + ": " + (realPage - 1).toString())
                    .runCommand("/ni help ${realPage - 1}")
            }
            val nextRaw = ComponentBuilder(ConfigManager.config.getString("Help.next") ?: "")
            if (realPage != pageAmount) {
                nextRaw.hoverText((ConfigManager.config.getString("Help.next") ?: "") + ": " + (realPage + 1))
                nextRaw.runCommand("/ni help ${realPage + 1}")
            }
            var listSuffixMessage = (ConfigManager.config.getString("Help.suffix") ?: "")
                .replace("{current}", realPage.toString())
                .replace("{total}", pageAmount.toString())
            val listMessage = ComponentBuilder("")
            if (sender is Player) {
                listSuffixMessage = listSuffixMessage
                    .replace("{prev}", "!@#$%{prev}!@#$%")
                    .replace("{next}", "!@#$%{next}!@#$%")
                val listSuffixMessageList = listSuffixMessage.split("!@#$%")
                listSuffixMessageList.forEach { value ->
                    when (value) {
                        "{prev}" -> listMessage.append(prevRaw)
                        "{next}" -> listMessage.append(nextRaw)
                        else -> listMessage.append(value)
                    }
                }
                // 向玩家发送信息
                sender.sendMessage(listMessage)
            } else {
                sender.sendMessage(
                    listSuffixMessage
                        .replace("{prev}", ConfigManager.config.getString("ItemList.Prev") ?: "")
                        .replace("{next}", ConfigManager.config.getString("ItemList.Next") ?: "")
                )
            }
        }
    }
}
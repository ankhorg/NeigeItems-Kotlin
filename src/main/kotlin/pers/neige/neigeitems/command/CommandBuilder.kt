package pers.neige.neigeitems.command

import bot.inker.bukkit.nbt.neigeitems.utils.ServerUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.utils.CommandUtils

class CommandBuilder(private val name: String) {
    private val command: PluginCommand = CommandUtils.newPluginCommand(name, plugin)!!

    private var nameSpace = name

    fun setPlugin(plugin: Plugin): CommandBuilder {
        CommandUtils.setPlugin(command, plugin)
        return this
    }

    fun setNameSpace(nameSpace: String): CommandBuilder {
        this.nameSpace = nameSpace
        return this
    }

    fun setExecutor(executor: CommandExecutor): CommandBuilder {
        command.setExecutor(executor)
        return this
    }

    fun setTabCompleter(tabCompleter: TabCompleter): CommandBuilder {
        command.tabCompleter = tabCompleter
        return this
    }

    fun setPermission(permission: String): CommandBuilder {
        command.permission = permission
        return this
    }

    fun setPermissionMessage(permissionMessage: String): CommandBuilder {
        command.permissionMessage = permissionMessage
        return this
    }

    fun setLabel(label: String): CommandBuilder {
        command.label = label
        return this
    }

    fun setAliases(aliases: List<String>): CommandBuilder {
        command.aliases = aliases
        return this
    }

    fun setDescription(description: String): CommandBuilder {
        command.description = description
        return this
    }

    fun setUsage(usage: String): CommandBuilder {
        command.usage = usage
        return this
    }

    fun register(): CommandBuilder {
        if (Bukkit.isPrimaryThread()) {
            CommandUtils.getCommandMap().register(nameSpace, command)
            ServerUtils.syncCommands()
        } else {
            Bukkit.getScheduler().callSyncMethod(plugin) {
                CommandUtils.getCommandMap().register(nameSpace, command)
                ServerUtils.syncCommands()
            }
        }
        return this
    }

    fun unregister(): CommandBuilder {
        // 这玩意儿必须同步跑, 不然服会爆炸
        if (Bukkit.isPrimaryThread()) {
            CommandUtils.unregisterCommand(name)
            CommandUtils.unregisterCommand("${nameSpace}:$name")
            command.aliases.forEach {
                CommandUtils.unregisterCommand(it)
                CommandUtils.unregisterCommand("${nameSpace}:$it")
            }
            ServerUtils.syncCommands()
        } else {
            Bukkit.getScheduler().callSyncMethod(plugin) {
                CommandUtils.unregisterCommand(name)
                CommandUtils.unregisterCommand("${nameSpace}:$name")
                command.aliases.forEach {
                    CommandUtils.unregisterCommand(it)
                    CommandUtils.unregisterCommand("${nameSpace}:$it")
                }
                ServerUtils.syncCommands()
            }
        }
        return this
    }
}
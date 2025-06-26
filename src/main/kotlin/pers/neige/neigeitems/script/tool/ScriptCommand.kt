package pers.neige.neigeitems.script.tool

import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ServerUtils
import pers.neige.neigeitems.manager.ExpansionManager
import pers.neige.neigeitems.utils.CommandUtils
import pers.neige.neigeitems.utils.SchedulerUtils.sync
import java.util.function.Consumer

/**
 * Bukkit 指令
 *
 * @property name 指令名
 * @constructor Bukkit 指令
 */
class ScriptCommand(rawName: String) {
    private val name = rawName.lowercase()

    /**
     * Bukkit PluginCommand 对象
     */
    val command: PluginCommand = CommandUtils.newPluginCommand(name, NeigeItems.getInstance())!!

    private var nameSpace = name

    /**
     * 编辑包装的 PluginCommand
     *
     * @param consumer 编辑器
     * @return ScriptCommand 本身
     */
    fun editCommand(consumer: Consumer<PluginCommand>): ScriptCommand {
        consumer.accept(command)
        return this
    }

    /**
     * 设置注册指令的插件
     *
     * @param plugin 任务
     * @return ScriptCommand 本身
     */
    fun setPlugin(plugin: Plugin): ScriptCommand {
        CommandUtils.setPlugin(command, plugin)
        return this
    }

    /**
     * 设置指令命名空间
     *
     * @param nameSpace 设置指令所需权限
     * @return ScriptCommand 本身
     */
    fun setNameSpace(nameSpace: String): ScriptCommand {
        this.nameSpace = nameSpace
        return this
    }

    /**
     * 设置指令执行器
     *
     * @param executor 指令执行器
     * @return ScriptCommand 本身
     */
    fun setExecutor(executor: CommandExecutor): ScriptCommand {
        command.setExecutor(executor)
        return this
    }

    /**
     * 设置指令补全函数
     *
     * @param tabCompleter 设置指令补全函数
     * @return ScriptCommand 本身
     */
    fun setTabCompleter(tabCompleter: TabCompleter): ScriptCommand {
        command.tabCompleter = tabCompleter
        return this
    }

    /**
     * 设置指令所需权限
     *
     * @param permission 设置指令所需权限
     * @return ScriptCommand 本身
     */
    fun setPermission(permission: String): ScriptCommand {
        command.permission = permission
        return this
    }

    /**
     * 设置无权限提示信息
     *
     * @param permissionMessage 无权限提示信息
     * @return ScriptCommand 本身
     */
    fun setPermissionMessage(permissionMessage: String): ScriptCommand {
        command.permissionMessage = permissionMessage
        return this
    }

    /**
     * 设置label
     *
     * @param label label
     * @return ScriptCommand 本身
     */
    fun setLabel(label: String): ScriptCommand {
        command.label = label
        return this
    }

    /**
     * 设置指令别名
     *
     * @param aliases 指令别名
     * @return ScriptCommand 本身
     */
    fun setAliases(aliases: List<String>): ScriptCommand {
        command.aliases = aliases
        return this
    }

    /**
     * 设置指令描述
     *
     * @param description 指令描述
     * @return ScriptCommand 本身
     */
    fun setDescription(description: String): ScriptCommand {
        command.description = description
        return this
    }

    /**
     * 设置指令用法(/help中显示)
     *
     * @param usage 指令用法
     * @return ScriptCommand 本身
     */
    fun setUsage(usage: String): ScriptCommand {
        command.usage = usage
        return this
    }

    /**
     * 注册指令
     *
     * @return ScriptCommand 本身
     */
    fun register(): ScriptCommand {
        // 存入ExpansionManager, 插件重载时自动取消注册
        ExpansionManager.commands["${nameSpace}:$name"]?.unregister()
        ExpansionManager.commands["${nameSpace}:$name"] = this
        // 这玩意儿必须同步跑, 不然服会爆炸
        sync {
            CommandUtils.getCommandMap().register(nameSpace, command)
            ServerUtils.syncCommands()
        }
        return this
    }

    /**
     * 取消注册指令
     *
     * @return ScriptCommand 本身
     */
    fun unregister(): ScriptCommand {
        // 这玩意儿必须同步跑, 不然服会爆炸
        sync {
            CommandUtils.unregisterCommand(name)
            CommandUtils.unregisterCommand("${nameSpace}:$name")
            command.aliases.forEach {
                CommandUtils.unregisterCommand(it)
                CommandUtils.unregisterCommand("${nameSpace}:$it")
            }
            ServerUtils.syncCommands()
        }
        return this
    }
}
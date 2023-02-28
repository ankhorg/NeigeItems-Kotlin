package pers.neige.neigeitems.script.tool

import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ExpansionManager
import taboolib.platform.BukkitCommand

/**
 * bukkit指令
 *
 * @property name 指令名
 * @constructor bukkit指令
 */
class ScriptCommand(val name: String) {
    /**
     * bukkit PluginCommand对象
     */
    val command: PluginCommand = with (BukkitCommand()) {
        sync()
        constructor.newInstance(name, plugin)
    }

    private var nameSpace = name

    /**
     * 设置指令命名空间
     *
     * @param nameSpace 设置指令所需权限
     * @return ScriptCommand本身
     */
    fun setNameSpace(nameSpace: String): ScriptCommand {
        this.nameSpace = nameSpace
        return this
    }

    /**
     * 设置指令执行器
     *
     * @param executor 指令执行器
     * @return ScriptCommand本身
     */
    fun setExecutor(executor: CommandExecutor): ScriptCommand {
        command.setExecutor(executor)
        return this
    }

    /**
     * 设置指令补全函数
     *
     * @param tabCompleter 设置指令补全函数
     * @return ScriptCommand本身
     */
    fun setTabCompleter(tabCompleter: TabCompleter): ScriptCommand {
        command.tabCompleter = tabCompleter
        return this
    }

    /**
     * 设置指令所需权限
     *
     * @param permission 设置指令所需权限
     * @return ScriptCommand本身
     */
    fun setPermission(permission: String): ScriptCommand {
        command.permission = permission
        return this
    }

    /**
     * 设置无权限提示信息
     *
     * @param permissionMessage 无权限提示信息
     * @return ScriptCommand本身
     */
    fun setPermissionMessage(permissionMessage: String): ScriptCommand {
        command.permissionMessage = permissionMessage
        return this
    }

    /**
     * 设置label
     *
     * @param label label
     * @return ScriptCommand本身
     */
    fun setLabel(label: String): ScriptCommand {
        command.label = label
        return this
    }

    /**
     * 设置指令别名
     *
     * @param aliases 指令别名
     * @return ScriptCommand本身
     */
    fun setAliases(aliases: List<String>): ScriptCommand {
        command.aliases = aliases
        return this
    }

    /**
     * 设置指令描述
     *
     * @param description 指令描述
     * @return ScriptCommand本身
     */
    fun setDescription(description: String): ScriptCommand {
        command.description = description
        return this
    }

    /**
     * 设置指令用法(/help中显示)
     *
     * @param usage 指令用法
     * @return ScriptCommand本身
     */
    fun setUsage(usage: String): ScriptCommand {
        command.usage = usage
        return this
    }

    /**
     * 注册指令
     *
     * @return ScriptCommand本身
     */
    fun register(): ScriptCommand {
        // 存入ExpansionManager, 插件重载时自动取消注册
        ExpansionManager.commands["${nameSpace}:$name"]?.unRegister()
        ExpansionManager.commands["${nameSpace}:$name"] = this
        // 这玩意儿必须同步跑, 不然服会爆炸
        bukkitScheduler.callSyncMethod(plugin) {
            val bukkitCommand = BukkitCommand()
            bukkitCommand.commandMap.register(nameSpace, command)
            bukkitCommand.sync()
        }
        return this
    }

    /**
     * 取消注册指令
     *
     * @return ScriptCommand本身
     */
    fun unRegister(): ScriptCommand {
        // 这玩意儿必须同步跑, 不然服会爆炸
        bukkitScheduler.callSyncMethod(NeigeItems.plugin) {
            val bukkitCommand = BukkitCommand()
            bukkitCommand.unregisterCommand(name)
            bukkitCommand.unregisterCommand("${nameSpace}:$name")
            command.aliases.forEach {
                bukkitCommand.unregisterCommand(it)
                bukkitCommand.unregisterCommand("${nameSpace}:$it")
            }
            bukkitCommand.sync()
        }
        return this
    }
}
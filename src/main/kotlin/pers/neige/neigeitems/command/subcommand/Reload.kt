package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import pers.neige.neigeitems.event.PluginReloadEvent
import pers.neige.neigeitems.manager.*
import pers.neige.neigeitems.utils.LangUtils.sendLang
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

object Reload {
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reloadCommand(sender)
        }
    }

    private fun reloadCommand(sender: CommandSender) {
        submit(async = true) {
            // 准备重载
            PluginReloadEvent.Pre().call()
            var time = System.currentTimeMillis()
            ConfigManager.reload()
            if (ConfigManager.debug) {
                println("配置重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            // ItemManager初始化的时候会将引用的全局节点写入ItemGenerator
            // 而全局节点由SectionManager加载
            // 所以SectionManager应该在ItemManager之前重载
            SectionManager.reload()
            if (ConfigManager.debug) {
                println("节点重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (ConfigManager.debug) {
                println("加载耗时大于1ms的物品将被提示:")
            }
            ItemManager.reload()
            if (ConfigManager.debug) {
                println("物品重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            ScriptManager.reload()
            if (ConfigManager.debug) {
                println("脚本重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            ItemPackManager.reload()
            if (ConfigManager.debug) {
                println("物品包重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            ActionManager.reload()
            if (ConfigManager.debug) {
                println("物品动作重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            ItemEditorManager.reload()
            if (ConfigManager.debug) {
                println("物品编辑函数重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            ExpansionManager.reload()
            if (ConfigManager.debug) {
                println("扩展重载耗时: ${System.currentTimeMillis() - time}ms")
            }

            // 重载完毕
            PluginReloadEvent.Post().call()
            sender.sendLang("Messages.reloadedMessage")
        }
    }
}
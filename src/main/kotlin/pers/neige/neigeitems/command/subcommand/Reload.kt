package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import pers.neige.neigeitems.event.PluginReloadEvent
import pers.neige.neigeitems.manager.*
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.subCommand

object Reload {
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reloadCommand(sender)
        }
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                arrayListOf(
                    "config",
                    "item",
                    "script",
                    "pack",
                    "action",
                    "editor",
                    "expansion",
                )
            }
            execute<CommandSender> { sender, _, argument ->
                reloadCommand(sender, argument)
            }
        }
    }

    private fun reloadCommand(sender: CommandSender, type: String? = null) {
        async {
            // 重载类型
            val reloadType = when (type) {
                "config" -> PluginReloadEvent.Type.CONFIG
                "item" -> PluginReloadEvent.Type.ITEM
                "script" -> PluginReloadEvent.Type.SCRIPT
                "pack" -> PluginReloadEvent.Type.PACK
                "action" -> PluginReloadEvent.Type.ACTION
                "editor" -> PluginReloadEvent.Type.EDITOR
                "expansion" -> PluginReloadEvent.Type.EXPANSION
                else -> PluginReloadEvent.Type.ALL
            }

            // 准备重载
            PluginReloadEvent.Pre(reloadType).call()
            var time = System.currentTimeMillis()

            if (type == null || reloadType == PluginReloadEvent.Type.CONFIG) {
                ConfigManager.reload()
                debug("配置重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (type == null || reloadType == PluginReloadEvent.Type.ITEM) {
                // ItemManager初始化的时候会将引用的全局节点写入ItemGenerator
                // 而全局节点由SectionManager加载
                // 所以SectionManager应该在ItemManager之前重载
                SectionManager.reload()
                debug("节点重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()

                debug("加载耗时大于1ms的物品将被提示:")
                ItemManager.reload()
                debug("物品重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (type == null || reloadType == PluginReloadEvent.Type.SCRIPT) {
                ScriptManager.reload()
                debug("脚本重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (type == null || reloadType == PluginReloadEvent.Type.PACK) {
                ItemPackManager.reload()
                debug("物品包重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (type == null || reloadType == PluginReloadEvent.Type.ACTION) {
                ActionManager.reload()
                debug("物品动作重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (type == null || reloadType == PluginReloadEvent.Type.EDITOR) {
                ItemEditorManager.reload()
                debug("物品编辑函数重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            if (type == null || reloadType == PluginReloadEvent.Type.EXPANSION) {
                ExpansionManager.reload()
                debug("扩展重载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }

            // 重载完毕
            PluginReloadEvent.Post(reloadType).call()
            sender.sendLang("Messages.reloadedMessage")
        }
    }
}
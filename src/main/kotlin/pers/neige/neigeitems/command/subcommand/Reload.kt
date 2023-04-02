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
            ConfigManager.reload()
            // ItemManager初始化的时候会将引用的全局节点写入ItemGenerator
            // 而全局节点由SectionManager加载
            // 所以SectionManager应该在ItemManager之前重载
            SectionManager.reload()
            ItemManager.reload()
            ScriptManager.reload()
            ItemPackManager.reload()
            ActionManager.reload()
            ItemEditorManager.reload()
            ExpansionManager.reload()
            // 重载完毕
            PluginReloadEvent.Post().call()
            sender.sendLang("Messages.reloadedMessage")
        }
    }
}
package pers.neige.neigeitems.command.impl

import org.bukkit.command.CommandSender
import pers.neige.neigeitems.manager.*
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang

object Reload {
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reloadCommand(sender)
        }
    }

    private fun reloadCommand(sender: CommandSender) {
        submit(async = true) {
            ConfigManager.reload()
            // ItemManager初始化的时候会将引用的全局节点写入ItemGenerator, 所以SectionManager应该在ItemManager之前重载
            SectionManager.reload()
            ScriptManager.reload()
            ItemManager.reload()
            ItemPackManager.reload()
            ActionManager.reload()
            ItemEditorManager.reload()
            sender.sendLang("Messages.reloadedMessage")
        }
    }
}
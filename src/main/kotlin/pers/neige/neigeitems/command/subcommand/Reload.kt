package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import pers.neige.colonel.argument
import pers.neige.colonel.literal
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.ReloadTypeArgument
import pers.neige.neigeitems.event.PluginReloadEvent
import pers.neige.neigeitems.manager.*
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni reload指令
 */
object Reload {
    @JvmStatic
    @CustomField(fieldType = "root")
    val reload = literal<CommandSender, Unit>("reload") {
        argument("type", ReloadTypeArgument.INSTANCE) {
            setNullExecutor { context ->
                async {
                    val type = context.getArgument<PluginReloadEvent.Type>("type")!!
                    // 准备重载
                    PluginReloadEvent.Pre(type).call()
                    var time = System.currentTimeMillis()

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.CONFIG) {
                        ConfigManager.reload()
                        debug("配置重载耗时: ${System.currentTimeMillis() - time}ms")
                        time = System.currentTimeMillis()
                    }

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.ITEM) {
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

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.SCRIPT) {
                        ScriptManager.reload()
                        debug("脚本重载耗时: ${System.currentTimeMillis() - time}ms")
                        time = System.currentTimeMillis()
                    }

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.PACK) {
                        ItemPackManager.reload()
                        debug("物品包重载耗时: ${System.currentTimeMillis() - time}ms")
                        time = System.currentTimeMillis()
                    }

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.ACTION) {
                        ActionManager.reload()
                        debug("物品动作重载耗时: ${System.currentTimeMillis() - time}ms")
                        time = System.currentTimeMillis()
                    }

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.EDITOR) {
                        ItemEditorManager.reload()
                        debug("物品编辑函数重载耗时: ${System.currentTimeMillis() - time}ms")
                        time = System.currentTimeMillis()
                    }

                    if (type == PluginReloadEvent.Type.ALL || type == PluginReloadEvent.Type.EXPANSION) {
                        ExpansionManager.reload()
                        debug("扩展重载耗时: ${System.currentTimeMillis() - time}ms")
                        time = System.currentTimeMillis()
                    }

                    // 重载完毕
                    PluginReloadEvent.Post(type).call()
                    context.source!!.sendLang("Messages.reloadedMessage")
                }
            }
        }
    }
}
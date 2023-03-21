package pers.neige.neigeitems.script

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import java.io.File

class ScriptExpansion : CompiledScript {
    /**
     * 构建JavaScript脚本扩展
     *
     * @property file js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(file: File) : super(file)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property script js脚本文本
     * @constructor JavaScript脚本扩展
     */
    constructor(script: String) : super(script)

    override fun loadLib() {
        scriptEngine.eval(
            """
                const Bukkit = Packages.org.bukkit.Bukkit
                const Command = Packages.pers.neige.neigeitems.script.tool.ScriptCommand
                const EventPriority = Packages.pers.neige.neigeitems.taboolib.common.platform.event.EventPriority
                const Listener = Packages.pers.neige.neigeitems.script.tool.ScriptListener
                const Placeholder = Packages.pers.neige.neigeitems.script.tool.ScriptPlaceholder
                const Task = Packages.pers.neige.neigeitems.script.tool.ScriptTask
                
                const ActionUtils = Packages.pers.neige.neigeitems.utils.ActionUtils
                const ConfigUtils = Packages.pers.neige.neigeitems.utils.ConfigUtils
                const FileUtils = Packages.pers.neige.neigeitems.utils.FileUtils
                const ItemUtils = Packages.pers.neige.neigeitems.utils.ItemUtils
                const JsonUtils = Packages.pers.neige.neigeitems.utils.JsonUtils
                const LangUtils = Packages.pers.neige.neigeitems.utils.LangUtils
                const PlayerUtils = Packages.pers.neige.neigeitems.utils.PlayerUtils
                const SamplingUtils = Packages.pers.neige.neigeitems.utils.SamplingUtils
                const ScriptUtils = Packages.pers.neige.neigeitems.utils.ScriptUtils
                const SectionUtils = Packages.pers.neige.neigeitems.utils.SectionUtils
                const StringUtils = Packages.pers.neige.neigeitems.utils.StringUtils
                const ActionManager = Packages.pers.neige.neigeitems.manager.ActionManager.INSTANCE
                const ConfigManager = Packages.pers.neige.neigeitems.manager.ConfigManager.INSTANCE
                const HookerManager = Packages.pers.neige.neigeitems.manager.HookerManager
                const ItemEditorManager = Packages.pers.neige.neigeitems.manager.ItemEditorManager.INSTANCE
                const ItemManager = Packages.pers.neige.neigeitems.manager.ItemManager.INSTANCE
                const ItemPackManager = Packages.pers.neige.neigeitems.manager.ItemPackManager.INSTANCE
                
                const plugin = Packages.pers.neige.neigeitems.NeigeItems.INSTANCE.plugin
                const pluginManager = Bukkit.getPluginManager()
                const scheduler = Bukkit.getScheduler()
                
                function sync(task) {
                    if (Bukkit.isPrimaryThread()) {
                        task()
                    } else {
                        scheduler.callSyncMethod(plugin, task)
                    }
                }
                
                function async(task) {
                    scheduler["runTaskAsynchronously(Plugin,Runnable)"](plugin, task)
                }
            """.trimIndent()
        )
    }

    /**
     * 是否包含加载时执行的函数
     */
    val enable = nashornHooker.isFunction(scriptEngine, "enable")

    /**
     * 是否包含加载时执行的函数
     */
    val serverEnable = nashornHooker.isFunction(scriptEngine, "serverEnable")

    /**
     * 是否包含卸载前执行的函数
     */
    val disable = nashornHooker.isFunction(scriptEngine, "disable")

    /**
     * 是否包含加载时执行的函数
     */
    val serverDisable = nashornHooker.isFunction(scriptEngine, "serverDisable")
}
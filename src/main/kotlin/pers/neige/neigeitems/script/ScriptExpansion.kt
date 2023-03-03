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
                
                const plugin = Packages.pers.neige.neigeitems.NeigeItems.INSTANCE.plugin
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
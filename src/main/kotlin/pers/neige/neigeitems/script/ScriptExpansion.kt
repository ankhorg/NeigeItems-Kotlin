package pers.neige.neigeitems.script

import org.bukkit.Bukkit
import pers.neige.neigeitems.manager.ConfigManager
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
                const MavenDependency = Packages.pers.neige.neigeitems.maven.MavenDependency
                const LocalDependency = Packages.pers.neige.neigeitems.maven.LocalDependency
                
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
     * 执行指定函数
     *
     * @param function 函数名
     * @param expansionName 脚本名称(默认为unnamed)
     */
    fun run(function: String, expansionName: String = "unnamed") {
        if (nashornHooker.isFunction(scriptEngine, function)) {
            try {
                invoke(function, null)
            } catch (error: Throwable) {
                Bukkit.getLogger().info(
                    ConfigManager.config.getString("Messages.expansionError")
                        ?.replace("{expansion}", expansionName)
                        ?.replace("{function}", function)
                )
                error.printStackTrace()
            }
        }
    }
}
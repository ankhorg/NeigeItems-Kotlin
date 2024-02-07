package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.event.PluginReloadEvent
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.script.ScriptExpansion
import pers.neige.neigeitems.script.tool.ScriptCommand
import pers.neige.neigeitems.script.tool.ScriptListener
import pers.neige.neigeitems.script.tool.ScriptPlaceholder
import pers.neige.neigeitems.script.tool.ScriptTask
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.SchedulerUtils.async
import java.io.File
import java.util.concurrent.ConcurrentHashMap


/**
 * 扩展管理器, 用于管理扩展脚本
 */
object ExpansionManager {
    /**
     * 所有永久脚本扩展<扩展名, 脚本扩展>
     */
    val permanentExpansion = ConcurrentHashMap<String, ScriptExpansion>()

    /**
     * 所有脚本扩展<文件名, 脚本扩展>
     */
    val expansions = ConcurrentHashMap<String, ScriptExpansion>()

    /**
     * 所有脚本扩展注册的指令
     */
    val commands = ConcurrentHashMap<String, ScriptCommand>()

    /**
     * 所有脚本扩展注册的监听器
     */
    val listeners = ConcurrentHashMap.newKeySet<ScriptListener>()

    /**
     * 所有脚本扩展注册的papi变量
     */
    val placeholders = ConcurrentHashMap<String, ScriptPlaceholder>()

    /**
     * 所有脚本扩展注册的Bukkit任务
     */
    val tasks = ConcurrentHashMap.newKeySet<ScriptTask>()

    init {
        load()
    }

    /**
     * 重载管理器
     */
    fun reload() {
        // 卸载脚本扩展
        unload()
        // 加载脚本扩展
        load()
    }

    /**
     * 卸载管理器
     */
    fun unload() {
        // 卸载脚本指令
        commands.values.forEach {
            it.unregister()
        }
        commands.clear()
        // 卸载脚本监听器
        listeners.forEach {
            it.unregister()
        }
        listeners.clear()
        // 卸载papi扩展
        placeholders.values.forEach {
            it.unregister()
        }
        placeholders.clear()
        // 卸载Bukkit任务
        tasks.forEach {
            it.unregister()
        }
        tasks.clear()
        // 清除脚本扩展
        expansions.clear()
    }

    /**
     * 添加永久脚本扩展
     *
     * @param expansionName 扩展名
     * @param expansion 脚本扩展
     */
    fun addPermanentExpansion(expansionName: String, expansion: ScriptExpansion) {
        permanentExpansion[expansionName] = expansion
    }

    /**
     * 加载脚本扩展
     */
    private fun load() {
        var time = System.currentTimeMillis()
        // 加载文件中的扩展
        for (file in ConfigUtils.getAllFiles("Expansions")) {
            // 这个不是真的文件名, 而是Expansions文件夹下的相对路径
            val fileName =
                file.path.replace("plugins${File.separator}NeigeItems${File.separator}Expansions${File.separator}", "")
            // 仅加载.js文件
            if (!fileName.endsWith(".js")) continue
            // 防止某个脚本出错导致加载中断
            try {
                // 加载脚本
                val script = ScriptExpansion(file)
                expansions[fileName] = script
            } catch (error: Throwable) {
                // 出错了就提示一下
                Bukkit.getLogger()
                    .info(ConfigManager.config.getString("Messages.invalidScript")?.replace("{script}", fileName))
                error.printStackTrace()
            } finally {
                // 开启了debug就发一下加载耗时
                debug("扩展-$fileName-加载耗时: ${System.currentTimeMillis() - time}ms")
                time = System.currentTimeMillis()
            }
        }
    }

    /**
     * 触发enable
     * PluginReloadEvent是异步触发的, 所以内部没有runTaskAsynchronously
     */
    @JvmStatic
    @Listener
    fun enable(event: PluginReloadEvent.Post) {
        if (event.type != PluginReloadEvent.Type.ALL && event.type != PluginReloadEvent.Type.EXPANSION) {
            return
        }
        permanentExpansion.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("enable", scriptName)
        }
        expansions.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("enable", scriptName)
        }
    }

    /**
     * 触发serverEnable(同时也会触发enable)
     * 内部runTaskAsynchronously了
     */
    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ACTIVE)
    private fun serverEnable() {
        async {
            permanentExpansion.forEach { (scriptName, scriptExpansion) ->
                scriptExpansion.run("enable", scriptName)
                scriptExpansion.run("serverEnable", scriptName)
            }
            expansions.forEach { (scriptName, scriptExpansion) ->
                scriptExpansion.run("enable", scriptName)
                scriptExpansion.run("serverEnable", scriptName)
            }
        }
    }

    /**
     * 触发disable
     * PluginReloadEvent是异步触发的, 所以内部没有runTaskAsynchronously
     */
    @JvmStatic
    @Listener
    fun disable(event: PluginReloadEvent.Pre) {
        if (event.type != PluginReloadEvent.Type.ALL && event.type != PluginReloadEvent.Type.EXPANSION) {
            return
        }
        permanentExpansion.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("disable", scriptName)
        }
        expansions.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("disable", scriptName)
        }
    }

    /**
     * 触发serverDisable(同时也会触发disable)
     * 关服的时候不能开新任务，所以是在主线程触发的
     */
    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.DISABLE)
    private fun serverDisable() {
        permanentExpansion.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("disable", scriptName)
            scriptExpansion.run("serverDisable", scriptName)
        }
        expansions.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("disable", scriptName)
            scriptExpansion.run("serverDisable", scriptName)
        }
        unload()
    }
}
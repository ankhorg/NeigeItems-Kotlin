package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import pers.neige.neigeitems.event.PluginReloadEvent
import pers.neige.neigeitems.script.ScriptExpansion
import pers.neige.neigeitems.script.tool.ScriptCommand
import pers.neige.neigeitems.script.tool.ScriptListener
import pers.neige.neigeitems.script.tool.ScriptPlaceholder
import pers.neige.neigeitems.script.tool.ScriptTask
import pers.neige.neigeitems.utils.ConfigUtils
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import java.io.File
import java.util.concurrent.ConcurrentHashMap


@RuntimeDependencies(
    RuntimeDependency(
        "!com.google.guava:guava:31.1-jre",
        test = "!com.google.common.collect.Sets.newConcurrentHashSet"
    )
)
/**
 * 扩展管理器, 用于管理扩展脚本
 */
object ExpansionManager {
    /**
     * 所有永久脚本扩展<扩展名, 脚本扩展>
     */
    val permanentExtension = ConcurrentHashMap<String, ScriptExpansion>()

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
        // 卸载脚本指令
        commands.values.forEach {
            it.unRegister()
        }
        commands.clear()
        // 卸载脚本监听器
        listeners.forEach {
            it.unRegister()
        }
        listeners.clear()
        // 卸载papi扩展
        placeholders.values.forEach {
            it.unRegister()
        }
        placeholders.clear()
        // 卸载Bukkit任务
        tasks.forEach {
            it.unRegister()
        }
        tasks.clear()
        // 清除脚本扩展
        expansions.clear()
        // 加载脚本扩展
        load()
    }

    /**
     * 添加永久脚本扩展
     *
     * @param expansionName 扩展名
     * @param expansion 脚本扩展
     */
    fun addPermanentExtension(expansionName: String, expansion: ScriptExpansion) {
        permanentExtension[expansionName] = expansion
    }

    /**
     * 加载脚本扩展
     */
    private fun load() {
        var time = System.currentTimeMillis()
        // 加载文件中的扩展
        for (file in ConfigUtils.getAllFiles("Expansions")) {
            // 这个不是真的文件名, 而是Expansions文件夹下的相对路径
            val fileName = file.path.replace("plugins${File.separator}NeigeItems${File.separator}Expansions${File.separator}", "")
            // 防止某个脚本出错导致加载中断
            try {
                // 加载脚本
                val script = ScriptExpansion(file)
                expansions[fileName] = script
            } catch (error: Throwable) {
                // 出错了就提示一下
                Bukkit.getLogger().info(ConfigManager.config.getString("Messages.invalidScript")?.replace("{script}", fileName))
                error.printStackTrace()
            } finally {
                // 开启了debug就发一下加载耗时
                if (ConfigManager.debug) {
                    val current = System.currentTimeMillis() - time
                    if (current > 1) {
                        Bukkit.getLogger().info("  扩展-$fileName-加载耗时: ${current}ms")
                    }
                    time = System.currentTimeMillis()
                }
            }
        }
    }

    /**
     * 触发enable
     * PluginReloadEvent是异步触发的, 所以内部没有runTaskAsynchronously
     */
    @SubscribeEvent(ignoreCancelled = true)
    fun enable(event: PluginReloadEvent.Post?) {
        permanentExtension.forEach { (scriptName, scriptExpansion) ->
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
    @Awake(LifeCycle.ACTIVE)
    fun serverEnable() {
        submit(async = true) {
            permanentExtension.forEach { (scriptName, scriptExpansion) ->
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
    @SubscribeEvent(ignoreCancelled = true)
    fun disable(event: PluginReloadEvent.Pre?) {
        permanentExtension.forEach { (scriptName, scriptExpansion) ->
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
    @Awake(LifeCycle.DISABLE)
    fun serverDisable() {
        permanentExtension.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("disable", scriptName)
            scriptExpansion.run("serverDisable", scriptName)
        }
        expansions.forEach { (scriptName, scriptExpansion) ->
            scriptExpansion.run("disable", scriptName)
            scriptExpansion.run("serverDisable", scriptName)
        }
    }
}
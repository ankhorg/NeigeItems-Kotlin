package pers.neige.neigeitems.manager

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
import taboolib.common.platform.event.EventPriority
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
        expansions.clear()
        load()
    }

    /**
     * 加载脚本扩展
     */
    private fun load() {
        for (file in ConfigUtils.getAllFiles("Expansions")) {
            // 防止某个脚本出错导致加载中断
            try {
                val script = ScriptExpansion(file)
                expansions[file.path.replace("plugins${File.separator}NeigeItems${File.separator}Expansions${File.separator}", "")] = script
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun enable(event: PluginReloadEvent.Post?) {
        expansions.values.forEach { scriptExpansion ->
            if (scriptExpansion.enable) {
                try {
                    scriptExpansion.invoke("enable", null)
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun serverEnable() {
        submit(async = true) {
            expansions.values.forEach { scriptExpansion ->
                if (scriptExpansion.enable) {
                    try {
                        scriptExpansion.invoke("enable", null)
                    } catch (error: Throwable) {
                        error.printStackTrace()
                    }
                }
                if (scriptExpansion.serverEnable) {
                    try {
                        scriptExpansion.invoke("serverEnable", null)
                    } catch (error: Throwable) {
                        error.printStackTrace()
                    }
                }
            }
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun disable(event: PluginReloadEvent.Pre?) {
        expansions.values.forEach { scriptExpansion ->
            if (scriptExpansion.disable) {
                try {
                    scriptExpansion.invoke("disable", null)
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun serverDisable() {
        expansions.values.forEach { scriptExpansion ->
            if (scriptExpansion.disable) {
                try {
                    scriptExpansion.invoke("disable", null)
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
            if (scriptExpansion.serverDisable) {
                try {
                    scriptExpansion.invoke("serverDisable", null)
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        }
    }
}
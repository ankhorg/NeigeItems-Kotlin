package pers.neige.neigeitems.manager

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import java.io.File
import java.io.FileReader
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.ScriptEngine

object ScriptManager {
    val scriptEngine = nashornHooker.getNashornEngine()

    val compiledScripts = HashMap<String, pers.neige.neigeitems.script.CompiledScript>()

    init {
        loadScripts()
    }

    private fun loadScripts() {
        // 加载全部脚本
        for (file in getAllFiles("Scripts")) {
            compiledScripts[file.name] = pers.neige.neigeitems.script.CompiledScript(FileReader(file))
        }
    }

    // 重载脚本管理器
    fun reload() {
        compiledScripts.clear()
        loadScripts()
    }

    fun newNashornEngine(): ScriptEngine {
        return nashornHooker.getNashornEngine()
    }

    val cache = ConcurrentHashMap<Int, CompiledScript>()

    val realCache = ConcurrentHashMap<Int, CompiledScript>()

    @Schedule(delay = (20 * 60 * 5).toLong(), period = (20 * 60 * 5).toLong(), async = true)
    private fun clear() {
        cache.clear()
    }
}
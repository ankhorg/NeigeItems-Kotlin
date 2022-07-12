package pers.neige.neigeitems.manager

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ScriptUtils
import pers.neige.neigeitems.utils.ScriptUtils.newContext
import taboolib.common.platform.Schedule
import java.io.File
import java.io.FileReader
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.ScriptEngine

object ScriptManager {
    val files: ArrayList<File> = ConfigUtils.getAllFiles("Scripts")

    val scriptEngine = nashornHooker.getNashornEngine()

    val compiledScripts = HashMap<String, pers.neige.neigeitems.script.CompiledScript>()

    fun newNashornEngine(): ScriptEngine {
        return nashornHooker.getNashornEngine()
    }

    val cache by lazy { ConcurrentHashMap<Int, CompiledScript>() }

    val realCache by lazy { ConcurrentHashMap<Int, CompiledScript>() }

    @Schedule(delay = (20 * 60 * 5).toLong(), period = (20 * 60 * 5).toLong(), async = true)
    private fun clear() {
        cache.clear()
    }

    init {
        scriptEngine.context = newContext()
        // 加载全部全局节点
        for (file in files) {
            compiledScripts[file.name] = pers.neige.neigeitems.script.CompiledScript(FileReader(file))
        }
    }
}
package pers.neige.neigeitems.manager

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import taboolib.common.platform.Schedule
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.ScriptEngine

object ScriptManager {
    val scriptEngine = nashornHooker.getNashornEngine()

    fun newNashornEngine(): ScriptEngine {
        return nashornHooker.getNashornEngine()
    }

    val cache by lazy { ConcurrentHashMap<Int, CompiledScript>() }

    val realCache by lazy { ConcurrentHashMap<Int, CompiledScript>() }

    @Schedule(delay = (20 * 60 * 5).toLong(), period = (20 * 60 * 5).toLong(), async = true)
    private fun clear() {
        cache.clear()
    }
}
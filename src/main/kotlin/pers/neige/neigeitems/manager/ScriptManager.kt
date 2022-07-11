package pers.neige.neigeitems.manager

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Schedule
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.ScriptEngine

@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
object ScriptManager {
    val scriptEngine: ScriptEngine by lazy {
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
            jdk.nashorn.api.scripting.NashornScriptEngineFactory().getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader)
        } catch (ex: ClassNotFoundException) {
            NashornScriptEngineFactory().getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader)
        }
    }

    val cache by lazy { ConcurrentHashMap<Int, CompiledScript>() }

    val realCache by lazy { ConcurrentHashMap<Int, CompiledScript>() }

    @Schedule(delay = (20 * 60 * 5).toLong(), period = (20 * 60 * 5).toLong(), async = true)
    private fun clear() {
        cache.clear()
    }
}
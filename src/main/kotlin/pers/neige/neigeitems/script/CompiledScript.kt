package pers.neige.neigeitems.script

import org.bukkit.OfflinePlayer
import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.io.Reader
import java.util.function.BiFunction
import javax.script.Invocable
import javax.script.ScriptEngine

class CompiledScript(reader: Reader) {
    val compiledScript = nashornHooker.compile(reader)

    val scriptEngine: ScriptEngine = compiledScript.engine

    fun invokeFunction(function: String, map: HashMap<String, Any>?, vararg args: Any): Any {
        map?.forEach { (key, value) -> scriptEngine.put(key, value) }
        compiledScript.eval(scriptEngine.context)
        return (scriptEngine as Invocable).invokeFunction(function, *args)
    }

    init {
        scriptEngine.put("papi", BiFunction<OfflinePlayer, String, String> { player, string ->
            papiHooker.papi(player, string)
        })
        scriptEngine.put("vars", java.util.function.Function<String, String> { it.parseSection() })
    }
}
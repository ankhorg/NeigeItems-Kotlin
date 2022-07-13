package pers.neige.neigeitems.script

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import java.io.Reader
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
}
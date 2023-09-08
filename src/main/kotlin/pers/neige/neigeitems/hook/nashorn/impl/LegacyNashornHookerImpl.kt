package pers.neige.neigeitems.hook.nashorn.impl

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import java.io.Reader
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine

/**
 * jdk自带nashorn挂钩
 *
 * @constructor 启用jdk自带nashorn挂钩
 */
class LegacyNashornHookerImpl : NashornHooker() {
    override fun getNashornEngine(args: Array<String>, classLoader: ClassLoader): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, classLoader)
    }

    override fun invoke(compiledScript: pers.neige.neigeitems.script.CompiledScript, function: String, map: Map<String, Any>?, vararg args: Any): Any? {
        val newObject: ScriptObjectMirror = (compiledScript.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror
        map?.forEach { (key, value) -> newObject[key] = value }
        return newObject.callMember(function, *args)
    }

    override fun isFunction(engine: ScriptEngine, func: Any?): Boolean {
        if (func is ScriptObjectMirror && func.isFunction) {
            return true
        }
        return false
    }
}
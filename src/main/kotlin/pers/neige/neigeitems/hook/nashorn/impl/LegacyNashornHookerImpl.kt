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
    override fun getNashornEngine(): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader)
    }

    override fun compile(string: String): CompiledScript {
        return (NashornScriptEngineFactory().getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader) as Compilable).compile(string)
    }

    override fun compile(reader: Reader): CompiledScript {
        return (NashornScriptEngineFactory().getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader) as Compilable).compile(reader)
    }

    override fun invoke(compiledScript: pers.neige.neigeitems.script.CompiledScript, function: String, map: HashMap<String, Any>?, vararg args: Any): Any? {
        val newObject: ScriptObjectMirror = (compiledScript.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror
        map?.forEach { (key, value) -> newObject[key] = value }
        return newObject.callMember(function, *args)
    }
}
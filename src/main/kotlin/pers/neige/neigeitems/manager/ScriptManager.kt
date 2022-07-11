package pers.neige.neigeitems.manager

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import pers.neige.neigeitems.item.ItemGenerator
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory

object ScriptManager {
    val scriptEngineFactory by lazy {
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory").getDeclaredConstructor().newInstance() as ScriptEngineFactory
        } catch (ex: ClassNotFoundException) {
            NashornScriptEngineFactory()
        }
    }

    val scriptEngine: ScriptEngine by lazy {
        scriptEngineFactory.scriptEngine
    }

    val cache by lazy { ConcurrentHashMap<Int, CompiledScript>() }
}
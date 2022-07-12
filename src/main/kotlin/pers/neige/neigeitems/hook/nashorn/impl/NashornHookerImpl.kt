package pers.neige.neigeitems.hook.nashorn.impl

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import javax.script.ScriptEngine

@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
class NashornHookerImpl : NashornHooker() {
    private val engineFactory = NashornScriptEngineFactory()

    override fun getNashornEngine(): ScriptEngine {
        return engineFactory.getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader)
    }
}
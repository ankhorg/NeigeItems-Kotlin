package pers.neige.neigeitems.hook.nashorn.impl

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import pers.neige.neigeitems.hook.nashorn.NashornHooker
import javax.script.ScriptEngine

class LegacyNashornHookerImpl : NashornHooker() {
    private val engineFactory = NashornScriptEngineFactory()

    override fun getNashornEngine(): ScriptEngine {
        return engineFactory.getScriptEngine(arrayOf("-Dnashorn.args=--language=es6"), this::class.java.classLoader)
    }
}
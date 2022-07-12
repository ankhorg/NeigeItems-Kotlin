package pers.neige.neigeitems.hook.nashorn

import java.io.Reader
import javax.script.CompiledScript
import javax.script.ScriptEngine

abstract class NashornHooker {
    abstract fun getNashornEngine(): ScriptEngine
    abstract fun compile(string: String): CompiledScript
    abstract fun compile(reader: Reader): CompiledScript
}
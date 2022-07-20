package pers.neige.neigeitems.hook.nashorn

import java.io.Reader
import javax.script.CompiledScript
import javax.script.ScriptEngine

abstract class NashornHooker {
    // 获取一个新的Nashorn引擎
    abstract fun getNashornEngine(): ScriptEngine

    // 编译一段js脚本
    abstract fun compile(string: String): CompiledScript

    // 编译一段js脚本
    abstract fun compile(reader: Reader): CompiledScript
}
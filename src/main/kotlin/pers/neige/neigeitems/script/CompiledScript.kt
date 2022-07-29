package pers.neige.neigeitems.script

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import java.io.Reader
import javax.script.Invocable
import javax.script.ScriptEngine

// 对已编译的js脚本进行包装, 便于调用其中的指定函数
class CompiledScript(reader: Reader) {
    // 已编译脚本
    val compiledScript = nashornHooker.compile(reader)

    // 该脚本对应的ScriptEngine
    val scriptEngine: ScriptEngine = compiledScript.engine

    /**
     * 执行脚本中的指定函数
     * @param function 函数名
     * @param map 传入的默认对象
     * @param args 传入对应方法的参数
     * @return 解析值
     */
    fun invoke(function: String, map: HashMap<String, Any>?, vararg args: Any): Any? {
        return nashornHooker.invoke(this, function, map, *args)
    }

    init {
        // 一段不可言说的代码, 将为脚本带来令人着迷的性能与高并发稳定性
        compiledScript.eval()
        scriptEngine.eval("""
            function NeigeItemsNumberOne() {}
            NeigeItemsNumberOne.prototype = this
            function newObject() { return new NeigeItemsNumberOne() }
        """)
    }
}
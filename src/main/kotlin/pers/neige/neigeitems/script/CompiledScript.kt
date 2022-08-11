package pers.neige.neigeitems.script

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.utils.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngine

/**
 * 编译js脚本并进行包装, 便于调用其中的指定函数
 *
 * @property file js脚本文件
 * @constructor 编译js脚本并进行包装
 */
class CompiledScript(file: File) {
    /**
     * 获取已编译脚本
     */
    val compiledScript = nashornHooker.compile(InputStreamReader(FileInputStream(file), FileUtils.charset(file)))

    /**
     * 获取该脚本对应的ScriptEngine
     */
    val scriptEngine: ScriptEngine = compiledScript.engine

    /**
     * 执行脚本中的指定函数
     *
     * @param function 函数名
     * @param map 传入的默认对象
     * @param args 传入对应方法的参数
     * @return 解析值
     */
    fun invoke(function: String, map: HashMap<String, Any>?, vararg args: Any): Any? {
        return nashornHooker.invoke(this, function, map, *args)
    }

    init {
        // 此段代码用于解决js脚本的高并发调用问题, 只可意会不可言传
        compiledScript.eval()
        scriptEngine.eval("""
            function NeigeItemsNumberOne() {}
            NeigeItemsNumberOne.prototype = this
            function newObject() { return new NeigeItemsNumberOne() }
        """)
    }
}
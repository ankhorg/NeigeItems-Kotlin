package pers.neige.neigeitems.hook.nashorn

import java.io.Reader
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine

/**
 * nashorn挂钩
 */
abstract class NashornHooker {
    /**
     * 获取一个新的Nashorn引擎
     *
     * @return 一个新的Nashorn引擎
     */
    abstract fun getNashornEngine(): ScriptEngine

    /**
     * 编译一段js脚本, 返回已编译脚本对象(将创建一个新的ScriptEngine用于解析脚本)
     *
     * @param string 待编译脚本文本
     * @return 已编译JS脚本
     */
    abstract fun compile(string: String): CompiledScript

    /**
     * 编译一段js脚本, 返回已编译脚本对象(将创建一个新的ScriptEngine用于解析脚本)
     *
     * @param reader 待编译脚本文件
     * @return 已编译JS脚本
     */
    abstract fun compile(reader: Reader): CompiledScript

    /**
     * 编译一段js脚本, 返回已编译脚本对象
     *
     * @param engine 用于编译脚本的脚本引擎
     * @param string 待编译脚本文本
     * @return 已编译JS脚本
     */
    fun compile(engine: ScriptEngine, string: String): CompiledScript {
        return (engine as Compilable).compile(string)
    }

    /**
     * 编译一段js脚本, 返回已编译脚本对象
     *
     * @param engine 用于编译脚本的脚本引擎
     * @param reader 待编译脚本文件
     * @return 已编译JS脚本
     */
    fun compile(engine: ScriptEngine, reader: Reader): CompiledScript {
        return (engine as Compilable).compile(reader)
    }

    /**
     * 调用脚本中的某个函数, 返回函数返回值
     * 因为内部涉及ScriptObjectMirror的使用, 所以放入nashornHooker
     *
     * @param compiledScript 待调用脚本
     * @param function 待调用函数名
     * @param map 顶级变量
     * @param args 传入函数的参数
     * @return 返回值
     */
    abstract fun invoke(compiledScript: pers.neige.neigeitems.script.CompiledScript, function: String, map: HashMap<String, Any>?, vararg args: Any): Any?
}
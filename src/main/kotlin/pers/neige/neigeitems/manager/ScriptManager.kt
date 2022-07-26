package pers.neige.neigeitems.manager

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import java.io.File
import java.io.FileReader
import javax.script.ScriptEngine

// 脚本文件管理器, 用于管理所有js节点的脚本文件, 同时提供公用ScriptEngine用于解析公式节点内容
object ScriptManager {
    // 公用ScriptEngine
    val scriptEngine = nashornHooker.getNashornEngine()

    // 所有已编译的js脚本文件
    val compiledScripts = HashMap<String, pers.neige.neigeitems.script.CompiledScript>()

    init {
        // 加载全部脚本
        loadScripts()
    }

    // 加载全部脚本
    private fun loadScripts() {
        for (file in getAllFiles("Scripts")) {
            compiledScripts[file.path.replace("plugins${File.separator}NeigeItems${File.separator}Scripts${File.separator}", "")] = pers.neige.neigeitems.script.CompiledScript(FileReader(file))
        }
    }

    // 重载脚本管理器
    fun reload() {
        compiledScripts.clear()
        loadScripts()
    }

    // 获取一个新的ScriptEngine
    fun newNashornEngine(): ScriptEngine {
        return nashornHooker.getNashornEngine()
    }

    // 测试表明, nashorn会自动进行相关优化
//    // 一级缓存, 用于记录被解析的公式, 五分钟清空一次
//    val cache = ConcurrentHashMap<Int, CompiledScript>()
//
//    // 二级缓存, 用于记录被重复解析过的公式, 持久化存储(预编译js脚本可以大大提升性能, 只缓存经过重复解析的脚本, 节省内存空间)
//    val realCache = ConcurrentHashMap<Int, CompiledScript>()
//
//    // 五分钟清空一次一级缓存
//    @Schedule(delay = (20 * 60 * 5).toLong(), period = (20 * 60 * 5).toLong(), async = true)
//    private fun clear() {
//        cache.clear()
//    }
}
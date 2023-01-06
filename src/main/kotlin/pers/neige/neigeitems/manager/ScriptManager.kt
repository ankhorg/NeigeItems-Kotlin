package pers.neige.neigeitems.manager

import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.script.CompiledScript
import pers.neige.neigeitems.section.impl.JoinParser
import pers.neige.neigeitems.section.impl.RepeatParser
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import java.io.File

/**
 * 脚本文件管理器, 用于管理所有js节点的脚本文件, 同时提供公用ScriptEngine用于解析公式节点内容
 *
 * @constructor 构建脚本文件管理器
 */
object ScriptManager {
    /**
     * 获取公用ScriptEngine
     */
    val scriptEngine = nashornHooker.getNashornEngine()

    /**
     * 获取所有已编译的js脚本文件及路径
     */
    val compiledScripts = HashMap<String, CompiledScript>()

    init {
        // 加载全部脚本
        loadScripts()
    }

    /**
     * 加载全部脚本
     */
    private fun loadScripts() {
        for (file in getAllFiles("Scripts")) {
            compiledScripts[file.path.replace("plugins${File.separator}NeigeItems${File.separator}Scripts${File.separator}", "")] = CompiledScript(file)
        }
    }

    /**
     * 重载脚本管理器
     */
    fun reload() {
        compiledScripts.clear()
        JoinParser.compiledScripts.clear()
        RepeatParser.compiledScripts.clear()
        loadScripts()
    }
}
package pers.neige.neigeitems.utils

import pers.neige.neigeitems.manager.ScriptManager.scriptEngine

/**
 * 脚本相关工具类
 */
object ScriptUtils {
    /**
     * 执行JS脚本
     *
     * @return 返回值
     */
    @JvmStatic
    fun String.eval(): Any {
        return scriptEngine.eval(this)
    }
}
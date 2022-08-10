package pers.neige.neigeitems.utils

import pers.neige.neigeitems.manager.ScriptManager.scriptEngine

object ScriptUtils {
    /**
     * 执行JS脚本
     * @return 返回值
     */
    @JvmStatic
    fun String.eval(): Any {
        return scriptEngine.eval(this)
    }
}
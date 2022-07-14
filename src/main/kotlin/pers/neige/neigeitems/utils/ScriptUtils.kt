package pers.neige.neigeitems.utils

import pers.neige.neigeitems.manager.ScriptManager.cache
import pers.neige.neigeitems.manager.ScriptManager.realCache
import pers.neige.neigeitems.manager.ScriptManager.scriptEngine
import javax.script.Compilable

object ScriptUtils {
    @JvmStatic
    fun String.eval(): Any {
        val hashCode = this.hashCode()
        // 已有缓存
        realCache[hashCode]?.let {
            return it.eval()
        }
        val compiledScript = (scriptEngine as Compilable).compile(this)
        compiledScript?.let {
            // 重复执行的脚本进入缓存
            if (cache.containsKey(hashCode)) {
                realCache[hashCode] = it
            } else {
                cache[hashCode] = it
            }
            return it.eval()
        }
        return this
    }
}
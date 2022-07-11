package pers.neige.neigeitems.utils

import pers.neige.neigeitems.manager.ScriptManager.cache
import pers.neige.neigeitems.manager.ScriptManager.scriptEngine
import javax.script.Compilable

object ScriptUtils {
    @JvmStatic
    fun String.eval(): Any {
        val hashCode = this.hashCode()
        cache[hashCode]?.let {
            return it.eval()
        }
        val compiledScript = (scriptEngine as Compilable).compile(this)
        compiledScript?.let {
            cache[hashCode] = it
            return it.eval()
        }
        return this
    }
}
package pers.neige.neigeitems.utils

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.manager.ScriptManager.cache
import pers.neige.neigeitems.manager.ScriptManager.realCache
import pers.neige.neigeitems.manager.ScriptManager.scriptEngine
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import javax.script.Bindings
import javax.script.Compilable
import javax.script.ScriptContext
import javax.script.SimpleScriptContext

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
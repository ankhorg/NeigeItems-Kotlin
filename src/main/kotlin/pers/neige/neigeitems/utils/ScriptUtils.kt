package pers.neige.neigeitems.utils

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager
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

    @JvmStatic
    fun vars(string: String, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        return string.parseSection(cache, player, sections)
    }

    fun newBindings(): Bindings {
        val bindings = scriptEngine.createBindings()
        bindings.putAll(mutableMapOf<String, Any>(
            Pair("papi", papiHooker::papi),
            Pair("getItem", ItemManager::getItemStack)
        ))
        return bindings
    }

    fun newContext(): SimpleScriptContext {
        val context = SimpleScriptContext()
        context.setAttribute("papi", papiHooker::papi, ScriptContext.ENGINE_SCOPE)
        context.setAttribute("vars", ScriptUtils::vars, ScriptContext.ENGINE_SCOPE)
        context.setAttribute("getItem", ItemManager::getItemStack, ScriptContext.ENGINE_SCOPE)
        return context
    }
}
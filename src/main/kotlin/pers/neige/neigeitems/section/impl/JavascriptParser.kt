package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ScriptManager
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.function.BiFunction

object JavascriptParser : SectionParser() {
    override val id: String = "js"

    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        data["path"]?.let {
            val array = (it as String).split("::")
            val path = array[0]
            val func = array[1]
            val map = HashMap<String, Any>()
            player?.let {
                map["player"] = player
                map["papi"] = java.util.function.Function<String, String> { string -> HookerManager.papiHooker.papi(player, string) }
            }
            map["vars"] = java.util.function.Function<String, String> { string -> string.parseSection(cache, player, sections) }
            return when (val args = data["args"]) {
                null -> ScriptManager.compiledScripts[path]?.invokeFunction(func, map)?.toString()
                else -> ScriptManager.compiledScripts[path]?.invokeFunction(func, map, args)?.toString()
            }
        }
        return null
    }

    override fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        val data = HashMap<String, Any>()
        if (args.isNotEmpty()) data["path"] = args[0]
        args.drop(1)
        if (args.isNotEmpty()) data["args"] = args
        return onRequest(data, cache, player, sections) ?: "<js::${args.joinToString("_")}>"
    }
}
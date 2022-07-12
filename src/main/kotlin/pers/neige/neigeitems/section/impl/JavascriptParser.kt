package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.SectionParser

object JavascriptParser : SectionParser() {
    override val id: String = "js"

    override fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
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
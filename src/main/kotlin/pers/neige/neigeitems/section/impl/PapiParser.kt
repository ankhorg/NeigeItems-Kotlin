package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.section.SectionParser

object PapiParser : SectionParser() {
    override val id: String = "papi"

    override fun onRequest(
        args: List<String>,
        cache: HashMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return player?.let { papi(player, "%${args.joinToString("_")}%") } ?: "<$id::${args.joinToString("_")}>"
    }
}
package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.entity.Player
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.platform.compat.PlaceholderExpansion

object Expansion : PlaceholderExpansion {
    override val identifier = "ni"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        val params = args.split("_")
        return when (params[0].lowercase()) {
            "parse" -> params.subList(1, params.size).joinToString("_").parseSection(player)
            else -> ""
        }
    }
}
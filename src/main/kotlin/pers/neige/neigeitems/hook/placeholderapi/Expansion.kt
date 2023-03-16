package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.entity.Player
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.platform.compat.PlaceholderExpansion

/**
 * PlaceholderAPI扩展
 */
object Expansion : PlaceholderExpansion {
    override val identifier = "ni"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        val params = args.split("_", limit = 2)
        return when (params[0].lowercase()) {
            "parse" -> params.getOrNull(1)?.parseSection(player) ?: ""
            else -> ""
        }
    }
}
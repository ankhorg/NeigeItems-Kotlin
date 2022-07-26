package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.entity.Player
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.function.pluginId
import taboolib.platform.compat.PlaceholderExpansion

object Expansion : PlaceholderExpansion {
    override val identifier = pluginId

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        val args = params.split("_")
        return when (args[0].lowercase()) {
            "parse" -> args.slice(1 until args.size).joinToString("_").parseSection(player)
            else -> ""
        }
    }
}
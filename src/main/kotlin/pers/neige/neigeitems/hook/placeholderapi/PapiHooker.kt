package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.OfflinePlayer

abstract class PapiHooker {
    abstract fun papi(player: OfflinePlayer, text: String): String
}
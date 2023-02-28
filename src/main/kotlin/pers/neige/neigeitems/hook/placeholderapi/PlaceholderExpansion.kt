package pers.neige.neigeitems.hook.placeholderapi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import java.util.function.BiFunction

class PlaceholderExpansion(
    val papiIdentifier: String,
    val papiAuthor: String,
    val papiVersion: String,
    val papiExecutor: BiFunction<OfflinePlayer, String, String>
) {
    val expansion: PlaceholderExpansion =
        object : PlaceholderExpansion() {
            override fun getIdentifier(): String {
                return papiIdentifier
            }

            override fun getAuthor(): String {
                return papiAuthor
            }

            override fun getVersion(): String {
                return papiVersion
            }

            override fun onRequest(player: OfflinePlayer, params: String): String {
                return papiExecutor.apply(player, params)
            }
        }

    fun register() {
        expansion.register()
    }

    fun unregister() {
        expansion.unregister()
    }
}
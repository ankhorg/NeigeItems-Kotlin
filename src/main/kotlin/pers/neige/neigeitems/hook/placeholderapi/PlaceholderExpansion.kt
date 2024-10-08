package pers.neige.neigeitems.hook.placeholderapi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import java.util.function.BiFunction

/**
 * 对PlaceholderExpansion的简单包装, 搭配HookerManager#papiHooker, 可以方便地注册PAPI变量
 */
class PlaceholderExpansion(
    val papiIdentifier: String,
    val papiAuthor: String,
    val papiVersion: String,
    val papiExecutor: BiFunction<OfflinePlayer?, String, String>
) {
    /**
     * 被包装的PAPI扩展
     */
    val expansion: PlaceholderExpansion = object : PlaceholderExpansion() {
        override fun getIdentifier(): String {
            return papiIdentifier
        }

        override fun getAuthor(): String {
            return papiAuthor
        }

        override fun getVersion(): String {
            return papiVersion
        }

        override fun onRequest(player: OfflinePlayer?, params: String): String {
            return papiExecutor.apply(player, params)
        }

        override fun onPlaceholderRequest(player: Player?, params: String): String {
            return papiExecutor.apply(player, params)
        }
    }

    /**
     * 注册扩展
     */
    fun register() {
        expansion.persist()
        expansion.register()
    }

    /**
     * 卸载扩展
     */
    fun unregister() {
        papiHooker?.unregisterExpansion(this)
    }
}
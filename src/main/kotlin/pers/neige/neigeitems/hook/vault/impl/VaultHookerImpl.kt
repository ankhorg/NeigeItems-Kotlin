package pers.neige.neigeitems.hook.vault.impl

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import pers.neige.neigeitems.hook.vault.VaultHooker

/**
 * Vault附属经济挂钩
 *
 * @constructor 启用Vault附属经济挂钩
 */
class VaultHookerImpl : VaultHooker() {
    private val economy = Bukkit.getServicesManager().getRegistration(Economy::class.java)?.provider

    override fun giveMoney(player: OfflinePlayer, amount: Double) {
        economy?.depositPlayer(player, amount)
    }

    override fun takeMoney(player: OfflinePlayer, amount: Double) {
        economy?.withdrawPlayer(player, amount)
    }

    override fun getMoney(player: OfflinePlayer): Double {
        return economy?.getBalance(player) ?: 0.toDouble()
    }
}
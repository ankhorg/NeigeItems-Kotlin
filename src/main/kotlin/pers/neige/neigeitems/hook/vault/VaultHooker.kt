package pers.neige.neigeitems.hook.vault

import org.bukkit.OfflinePlayer

abstract class VaultHooker {
    abstract fun giveMoney(player: OfflinePlayer, amount: Double)

    abstract fun takeMoney(player: OfflinePlayer, amount: Double)

    abstract fun getMoney(player: OfflinePlayer): Double
}
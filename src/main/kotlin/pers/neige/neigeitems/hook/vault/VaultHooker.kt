package pers.neige.neigeitems.hook.vault

import org.bukkit.OfflinePlayer

/**
 * Vault附属经济挂钩
 */
abstract class VaultHooker {
    /**
     * 给予玩家Vault金钱
     *
     * @param player 待操作玩家
     * @param amount 给予金钱数
     */
    abstract fun giveMoney(player: OfflinePlayer, amount: Double)

    /**
     * 扣除玩家Vault金钱
     *
     * @param player 待操作玩家
     * @param amount 扣除金钱数
     */
    abstract fun takeMoney(player: OfflinePlayer, amount: Double)

    /**
     * 获取玩家Vault金钱
     *
     * @param player 待操作玩家
     * @return 玩家当前金钱
     */
    abstract fun getMoney(player: OfflinePlayer): Double
}
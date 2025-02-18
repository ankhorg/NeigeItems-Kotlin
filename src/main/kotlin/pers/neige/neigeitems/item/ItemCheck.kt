package pers.neige.neigeitems.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.ItemExpirationEvent
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.checkCooldown

/**
 * 物品检测器, 用于回收过期物品并更新旧版物品
 */
object ItemCheck {
    /**
     * 检查物品
     *
     * @param player 待检查玩家
     * @param itemStack 待检查物品
     */
    fun checkItem(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo
    ) {
        kotlin.runCatching {
            val neigeItems = itemInfo.neigeItems
            // 检测过期物品
            if (
                System.currentTimeMillis() >= neigeItems.getLong("itemTime", Long.MAX_VALUE)
                && ItemExpirationEvent(player, itemStack, itemInfo).call()
            ) {
                val itemName = itemStack.getName()
                itemStack.amount = 0
                player.sendLang("Messages.itemExpirationMessage", mapOf("{itemName}" to itemName))
                // 都过期了，就不用更新了
                return
            }
            // 物品更新
            ItemManager.update(player, itemStack, sendMessage = true)
        }
    }

    /**
     * 当前是否可以检查物品(最多一秒检查一次玩家背包, 防止服务器噶了)
     */
    fun Player.couldCheckInventory(): Boolean {
        if (!ConfigManager.checkInventory) return false
        return checkCooldown("ni:check_inv", 1000) <= 0
    }
}
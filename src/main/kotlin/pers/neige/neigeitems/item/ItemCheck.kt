package pers.neige.neigeitems.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.ItemExpirationEvent
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ

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
            val id = itemInfo.id
            val itemTag = itemInfo.itemTag
            val neigeItems = itemInfo.neigeItems
            val nbtItemStack = itemInfo.nbtItemStack
            // 检测过期物品
            if (neigeItems.containsKey("itemTime")) {
                if (System.currentTimeMillis() >= neigeItems.getLong("itemTime")) {
                    val event = ItemExpirationEvent(player, itemStack, itemInfo)
                    event.call()
                    if (event.isCancelled) return
                    val itemName = itemStack.getName()
                    itemStack.amount = 0
                    config.getString("Messages.itemExpirationMessage")
                        ?.replace("{itemName}", itemName)
                        ?.let(player::sendMessage)
                    // 都过期了，就不用更新了
                    return
                }
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
        // 获取当前时间
        val time = System.currentTimeMillis()
        // 获取上次检查时间
        val lastTime = this.getMetadataEZ("NI-CheckInvCD", 0.toLong()) as Long
        // 如果仍处于冷却时间
        if ((lastTime + 1000) > time) {
            return false
        }
        this.setMetadataEZ("NI-CheckInvCD", time)
        return true
    }
}
package pers.neige.neigeitems.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryOpenEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.item.ItemCheck.couldCheckInventory
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object InventoryOpenListener {
    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    private fun listener(event: InventoryOpenEvent) {
        val player = event.player as Player
        val inventory = event.inventory
        // 如果允许检测物品过期
        if (player.couldCheckInventory()) {
            inventory.contents.forEach { itemStack ->
                // 获取NI物品信息(不是NI物品就停止操作)
                val itemInfo = itemStack?.isNiItem()
                if (itemInfo != null) {
                    // 检测物品过期, 检测物品更新
                    ItemCheck.checkItem(player, itemStack, itemInfo)
                }
            }
        }
    }
}
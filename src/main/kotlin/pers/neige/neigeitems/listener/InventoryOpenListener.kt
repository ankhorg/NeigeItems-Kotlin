package pers.neige.neigeitems.listener

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.item.ItemCheck.couldCheckInventory
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag

object InventoryOpenListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: InventoryOpenEvent) {
        val player = event.player as Player
        val inventory = event.inventory
        // 如果允许检测物品过期
        if (player.couldCheckInventory()) {
            inventory.contents.forEach { itemStack ->
                // 获取NI物品信息(不是NI物品就停止操作)
                val itemInfo = itemStack?.isNiItem(true)
                if (itemInfo != null) {
                    // 物品NBT
                    val itemTag: ItemTag = itemInfo.itemTag
                    // NI物品数据
                    val neigeItems: ItemTag = itemInfo.neigeItems
                    // NI物品id
                    val id: String = itemInfo.id
                    // NI节点数据
                    val data: HashMap<String, String> = itemInfo.data!!

                    // 检测物品过期, 检测物品更新
                    ItemCheck.checkItem(player, itemStack, itemInfo, itemTag, neigeItems, id, data)
                }
            }
        }
    }
}
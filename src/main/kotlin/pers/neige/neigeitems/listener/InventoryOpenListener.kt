package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.item.ItemCheck.couldCheckInventory
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

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
                    // 检测物品过期, 检测物品更新
                    ItemCheck.checkItem(player, itemStack, itemInfo)
                }
            }
        }
    }
}
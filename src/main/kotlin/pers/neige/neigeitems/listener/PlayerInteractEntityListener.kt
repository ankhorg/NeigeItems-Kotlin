package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag

object PlayerInteractEntityListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerInteractEntityEvent) {
        val player = event.player
        // 对于已损坏物品取消事件
        val itemStack = when(event.hand) {
            EquipmentSlot.HAND -> player.inventory.itemInMainHand
            else -> player.inventory.itemInOffHand
        }
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(true) ?: return
        // 物品NBT
        val itemTag: ItemTag = itemInfo.itemTag
        // NI物品数据
        val neigeItems: ItemTag = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        // NI节点数据
        val data: HashMap<String, String> = itemInfo.data!!

        // 检测已损坏物品
        ItemDurability.interact(player, neigeItems, event)
        if (event.isCancelled) return
    }
}
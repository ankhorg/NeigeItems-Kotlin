package pers.neige.neigeitems.listener

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag

object InventoryClickListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun click(event: InventoryClickEvent) {
        // 获取玩家
        val player = event.whoClicked
        if (player !is Player) return
        // 获取点击物品
        val itemStack = event.cursor
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem(true) ?: return
        // 物品NBT
        val itemTag: ItemTag = itemInfo.itemTag
        // NI物品数据
        val neigeItems: ItemTag = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        // NI节点数据
        val data: HashMap<String, String> = itemInfo.data!!

        ActionManager.clickListener(player, itemStack, itemTag, neigeItems, id, data, event)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun beClicked(event: InventoryClickEvent) {
        // 获取玩家
        val player = event.whoClicked
        if (player !is Player) return
        // 获取点击物品
        val itemStack = event.currentItem
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem(true) ?: return
        // 物品NBT
        val itemTag: ItemTag = itemInfo.itemTag
        // NI物品数据
        val neigeItems: ItemTag = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        // NI节点数据
        val data: HashMap<String, String> = itemInfo.data!!

        // 执行物品动作
        ActionManager.beClickedListener(player, itemStack, itemTag, neigeItems, id, data, event)
    }
}
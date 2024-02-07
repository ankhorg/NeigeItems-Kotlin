package pers.neige.neigeitems.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object InventoryClickListener {
    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    fun click(event: InventoryClickEvent) {
        // 获取玩家
        val player = event.whoClicked
        if (player !is Player) return
        // 获取点击物品
        val itemStack = event.cursor
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem() ?: return

        // 执行物品动作
        ActionManager.clickListener(player, itemStack, itemInfo, event)
    }

    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    fun beClicked(event: InventoryClickEvent) {
        // 获取玩家
        val player = event.whoClicked
        if (player !is Player) return
        // 获取点击物品
        val itemStack = event.currentItem
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem() ?: return

        // 执行物品动作
        ActionManager.beClickedListener(player, itemStack, itemInfo, event)
    }
}